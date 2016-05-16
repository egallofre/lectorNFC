package edu.soft.lectornfc;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button llegir;
    EditText entry,entryHourTXT,exitDay,exitHour;
    CheckBox check,check2;
    TextView txt;
    Tag myTag;
    Context context;
    NfcAdapter adapter;
    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    String dataAgafada,dataAgafada2,horaAgafada,dataAgafada3,horaAgafada2;
    String url;
    String insertat;



    Calendar calendar=Calendar.getInstance();


    DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {

            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            String currentTime = sdf2.format(calendar.getTime());

            dataAgafada=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            dataAgafada2=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;

            entry.setText(currentTime);
        }
    };

    DatePickerDialog.OnDateSetListener listener3=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {

            calendar.set(Calendar.MONTH,monthOfYear);
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
            String currentTime = sdf2.format(calendar.getTime());


            dataAgafada3=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            dataAgafada2=dayOfMonth+"/"+(monthOfYear+1)+"/"+year;

            exitDay.setText(currentTime);
        }
    };

    TimePickerDialog.OnTimeSetListener listener2= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            horaAgafada=selectedHour+":"+selectedMinute;
            entryHourTXT.setText(horaAgafada);
        }

    };

    TimePickerDialog.OnTimeSetListener listener4= new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
            horaAgafada2=selectedHour+":"+selectedMinute;
            exitHour.setText(horaAgafada2);
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        llegir = (Button) findViewById(R.id.button);
        txt = (TextView) findViewById(R.id.textView);
        entry=(EditText) findViewById(R.id.entryDay);
        check=(CheckBox) findViewById(R.id.checkBox);
        check2=(CheckBox) findViewById(R.id.checkBox2);
        //entryHour=(Button)findViewById(R.id.entryHourBTN);
        entryHourTXT=(EditText)findViewById(R.id.entryHour);
        exitDay=(EditText)findViewById(R.id.exitDay);
        exitHour=(EditText)findViewById(R.id.exitHour);

        adapter = NfcAdapter.getDefaultAdapter(this);




        if (adapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!adapter.isEnabled()) {
            Toast.makeText(this, "NFC is disabled", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "NFC is enabled", Toast.LENGTH_LONG).show();

        }



        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentTime = sdf.format(new Date());


        entry.setHint(currentTime);


    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    protected void onNewIntent(Intent intent) {
        // TODO: handle
        Toast.makeText(this,"NFC intent receiver!", Toast.LENGTH_LONG).show();
        Tag tag =intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


        txt.setText("" + bytesToHex(tag.getId()));
        super.onNewIntent(intent);
    }

    protected void onResume() {
        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        IntentFilter[] intentFilter=new IntentFilter[]{};
        adapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);
        super.onResume();
    }
    protected void onPause(){
        adapter.disableForegroundDispatch(this);
        super.onPause();
    }
    public void onReadBTN(View view)
    {
        char xiv='1';
        if (txt.getText().equals(""))
        {
            Toast.makeText(this,"ID NFC is empty.", Toast.LENGTH_LONG).show();
            xiv=0;
        }
       if (xiv=='1') {
        char autho='0';
        char always='0';
        if (check.isChecked())
        {
            autho='1';
        }
        if (check2.isChecked())
        {
            always='1';
        }
        url="http://www.dorigen.cat/lectorNFC/insertar.php?cardId="+txt.getText().toString()+"&authorised="+autho+"&always="+always+"&entryDay="+dataAgafada+"&exitDay="+dataAgafada3+"&entryHour="+horaAgafada+"&exitHour="+horaAgafada2;


           Thread nt = new Thread() {
               @Override
               public void run() {

                   HttpClient httpClient = new DefaultHttpClient();
                   StringBuilder builder = new StringBuilder();
                   JSONArray jarray = null;

                   HttpGet getData = new HttpGet(url);
                   try {
                       HttpResponse resp = httpClient.execute(getData);
                       StatusLine statusLine = resp.getStatusLine();
                       int statusCode = statusLine.getStatusCode();
                       if (statusCode == 200) {
                           HttpEntity entity = resp.getEntity();
                           InputStream content = entity.getContent();
                           BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                           String line;


                           while ((line = reader.readLine()) != null) {
                               builder.append(line);

                           }
                       } else {
                           Log.e("-->", "error downloading");

                       }

                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   try {
                       jarray = new JSONArray(builder.toString());

                   } catch (JSONException e) {
                       Log.e("Json", "JSON error translating data" + e.toString());

                   }

                   JSONObject obj;

                   try {
                       obj = jarray.getJSONObject(0);
                       insertat = obj.getString("validar");
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }

                   //}

                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {

                           if (insertat.equals("si")) {
                               Toast.makeText(MainActivity.this, "Record inserted correctly ", Toast.LENGTH_LONG).show();
                               txt.setText("");

                           }
                           if (insertat.equals("duplicat")) {
                               Toast.makeText(MainActivity.this, "There is already a record with this ID in te database ", Toast.LENGTH_LONG).show();

                           }
                       }


                   });

               }
           };
           nt.start();
       }
    }
    public void diaInicial(View view)
    {
        new DatePickerDialog(MainActivity.this, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    public void btnentryHour(View view)
    {
        new TimePickerDialog(MainActivity.this, listener2, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
    }
    public void exitDayBTN(View view)
    {
        new DatePickerDialog(MainActivity.this, listener3, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
    public void exitHourBTN(View view)
    {
        new TimePickerDialog(MainActivity.this, listener4, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
    }

}








