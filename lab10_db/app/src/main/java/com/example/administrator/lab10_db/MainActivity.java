package com.example.administrator.lab10_db;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public final String DB = "app.db";
    Button btn_add;
    ListView person_listView;
    ArrayList<HashMap<String, String>> person_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        btn_add = (Button)findViewById(R.id.btn_add);
        person_listView = (ListView) findViewById(R.id.person_list);
        person_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setMessage("是否删除？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String name = person_list.get(index).get("name");
                        myHelper helper = getHelper();
                        helper.delete(name);
                        updatePersonListView();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // nothing
                    }
                }).show();

                return true;
            }
        });
        person_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> m = person_list.get(i);
                final Person person = new Person(m.get("name"), m.get("birthday"), m.get("gift"));
                LayoutInflater factor = LayoutInflater.from(MainActivity.this);
                View view_in = factor.inflate(R.layout.dialog, null);

                TextView person_name = (TextView)view_in.findViewById(R.id.name);
                final EditText ed_birthday = (EditText)view_in.findViewById(R.id.ed_birthday);
                final EditText ed_gift = (EditText)view_in.findViewById(R.id.ed_gift);
                final TextView phone = (TextView)view_in.findViewById(R.id.phone);

                person_name.setText(person.getName());
                ed_birthday.setText(person.getBirthday());
                ed_gift.setText(person.getGift());

                Cursor cursor =getContentResolver().query(ContactsContract.
                        Contacts.CONTENT_URI,null,
                        ContactsContract.Contacts.DISPLAY_NAME + "=?",
                        new String[]{ person.getName() },null);

                while(cursor.moveToNext()){
                    String id =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name =cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.i("查看联系人", name + " has id: " + id);
                    int isHas =Integer.parseInt(cursor.getString(cursor.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                    if(isHas>0){
                        Cursor c =getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " +id,
                                null,null);
                        while(c.moveToNext()){
                            String number =c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            phone.setText(number);
                        }
                        c.close();
                    }
                }
                cursor.close();



                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(view_in);
                builder.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        myHelper helper = getHelper();
                        Person new_person = new Person(person.getName(), ed_birthday.getText().toString(), ed_gift.getText().toString());
                        helper.update(person.getName(), new_person);
                        updatePersonListView();
                    }
                }).setNegativeButton("放弃修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, add_person.class));
            }
        });
        updatePersonListView();
    }

    private void updatePersonListView() {
        myHelper helper = getHelper();
        person_list = helper.getPersons();
        person_listView.setAdapter(new SimpleAdapter(MainActivity.this, person_list, R.layout.item,
                new String[]{ "name", "birthday", "gift" }, new int[]{ R.id.tv_name, R.id.tv_birthday, R.id.gift }));

    }

    private void insertToPersonList (Person person) {
        HashMap<String, String> m = new HashMap<String, String>();
        m.put("name", person.getName());
        m.put("birthday", person.getBirthday());
        m.put("gift", person.getGift());
        person_list.add(m);
    }

    private myHelper getHelper() {
        return new myHelper(this, DB, null, 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(MessageEvent event) {
        myHelper helper = getHelper();
        Person person = event.person;
        helper.insert(person);
        updatePersonListView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
