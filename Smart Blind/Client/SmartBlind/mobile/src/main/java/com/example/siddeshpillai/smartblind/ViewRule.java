package com.example.siddeshpillai.smartblind;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.rit.csci759.jsonrpc.server.Rules;
import edu.rit.csci759.jsonrpc.server.RuleList;

/**
 * Created by siddeshpillai on 10/7/15.
 */
public class ViewRule extends Activity {

    // Member variables
    ImageButton add = null;
    ListView viewAllRules = null;
    List<Rules> rulesList = null;
    ItemsListAdapter myItemsListAdapter;
    RuleList allrules = null;
    private ProgressBar progressBar;
    List<String> listNames;
    HashMap<Integer, List<String>> allRulesMap = new HashMap<Integer, List<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rules);
        add = (ImageButton) findViewById(R.id.add_button); // onclickListener

        rulesList = new ArrayList<Rules>();
        viewAllRules = (ListView) findViewById(R.id.viewAllRulesView);
        try {
            new AsyncAction().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ViewRule.this,
                        AddRuleActivity.class);
                myIntent.putExtra("allrules", allrules);
                startActivity(myIntent);
            }
        });

        myItemsListAdapter = new ItemsListAdapter(this, new ArrayList<String>());
        viewAllRules.setAdapter(myItemsListAdapter);

        viewAllRules.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(ViewRule.this,
                        EditRuleActivity.class);
                myIntent.putExtra("allrules", allrules);
                myIntent.putExtra("currentrule", position);

                startActivity(myIntent);

            }
        });
    }


    /**
     * The items list adapter
     */
    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<String> list;

        ItemsListAdapter(Context c, List<String> l) {
            context = c;
            list = l;
        }

        public void setItemList(List<String> itemList) {
            this.list = itemList;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;

            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.row, null);

                ViewHolder viewHolder = new ViewHolder();
                viewHolder.text = (TextView) rowView.findViewById(R.id.rowTextView);
                rowView.setTag(viewHolder);
            }

            ViewHolder holder = (ViewHolder) rowView.getTag();
            holder.text.setText(list.get(position));

            return rowView;
        }

    }

    /**
     * The view holder for text
     */
    static class ViewHolder {
        TextView text;
    }

    /**
     * The Async loads all the rules
     */
    private class AsyncAction extends AsyncTask<Void, Void, Void> {

        public static final String SERVER_IP = "10.10.10.105";
        public static final int SERVER_PORT = 8080;
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        private final ProgressDialog dialog = new ProgressDialog(ViewRule.this);

        HashMap<Integer, List<String>> allRulesMap = new HashMap<Integer, List<String>>();

        Socket socket = null;
        String view = new String("view");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Fetching rules...");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {


            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);

                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(socket.getInputStream());

                oos.writeObject(view);
//                oos.flush();

                allRulesMap = (HashMap<Integer, List<String>>) ois.readObject();


            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            List<String> glist = new ArrayList<String>();
            for (int i = 0; i < allRulesMap.size(); i++) {
                glist = allRulesMap.get(i + 1);
                Rules ru = new Rules(i, glist.get(0), glist.get(1), glist.get(2), glist.get(3), glist.get(4), glist.get(5));
                rulesList.add(ru);
            }
            listNames = new ArrayList<String>();

            for (int i = 0; i < rulesList.size(); i++) {
                listNames.add("Rule " + (i + 1));
            }
            allrules = new RuleList();
            allrules.setRuleList(rulesList);


            dialog.dismiss();
            myItemsListAdapter.setItemList(listNames);
            myItemsListAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            myItemsListAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
