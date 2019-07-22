package sg.edu.rp.c346.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etTelephone;
    EditText etSize;
    EditText etDate;
    EditText etTime;
    CheckBox checkBox;
    Button btReserve;
    Button btReset;

    int updatedYear = 0;
    int updatedMonth = 0;
    int updatedDay = 0;

    int updatedHour = 0;
    int updatedMinute = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("Reservation");

        etName = findViewById(R.id.editTextName);
        etTelephone = findViewById(R.id.editTextTelephone);
        etSize = findViewById(R.id.editTextSize);
        etDate = findViewById(R.id.editTextDate);
        etTime = findViewById(R.id.editTextTime);
        checkBox = findViewById(R.id.checkBox);
        btReserve = findViewById(R.id.buttonReserve);
        btReset = findViewById(R.id.buttonReset);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        etDate.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                    }
                };

                String[] date = etDate.getText().toString().split("/");

                if (updatedYear == 0 || etDate.getText().toString() == null) {
                    Calendar curDate = Calendar.getInstance();

                    updatedDay = curDate.get(Calendar.DAY_OF_MONTH);
                    updatedMonth = curDate.get(Calendar.MONTH);
                    updatedYear = curDate.get(Calendar.YEAR);

                }else{

                    updatedDay = Integer.parseInt(date[0]);
                    updatedMonth = Integer.parseInt(date[1])-1;
                    updatedYear = Integer.parseInt(date[2]);

                }

                DatePickerDialog myDateDialog = new DatePickerDialog(MainActivity.this,
                        myDateListener, updatedYear, updatedMonth, updatedDay);
                myDateDialog.show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etTime.setText(hourOfDay + ":" + minute);
                    }
                };

                String[] time = etTime.getText().toString().split(":");

                if (updatedHour == 0 || etTime.getText().toString() == null) {
                    Calendar curDate = Calendar.getInstance();

                    updatedHour = curDate.get(Calendar.HOUR_OF_DAY);
                    updatedMinute = curDate.get(Calendar.MINUTE);

                }else{

                    updatedHour = Integer.parseInt(time[0]);
                    updatedMinute = Integer.parseInt(time[1]);

                }

                TimePickerDialog myTimeDialog = new TimePickerDialog(MainActivity.this,
                        myTimeListener, updatedHour, updatedMinute, false);

                myTimeDialog.show();
            }
        });


        btReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String isSmoke = "";
                if (checkBox.isChecked()) {
                    isSmoke = "smoking";
                } else {
                    isSmoke = "non-smoking";
                }

                AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);
                myBuilder.setTitle("Reservation Confirmation");

                myBuilder.setMessage("New Reservation"
                                + "\nName: " + etName.getText().toString()
                                + "\nSmoking: " + isSmoke
                                + "\nSize: " + etSize.getText().toString()
                                + "\nDate: " + etDate.getText().toString()
                                + "\nTime: " + etTime.getText().toString());

                myBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(MainActivity.this, "SMS has been Sent", Toast.LENGTH_SHORT).show();

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                        SharedPreferences.Editor prefEdit = prefs.edit();

                        prefEdit.putString("name", etName.getText().toString());
                        prefEdit.putBoolean("smoke", checkBox.isChecked());
                        prefEdit.putString("size", etSize.getText().toString());
                        prefEdit.putString("date", etDate.getText().toString());
                        prefEdit.putString("time", etTime.getText().toString());
                        prefEdit.putString("phone", etTelephone.getText().toString());
                        prefEdit.commit();


                    }
                });

                myBuilder.setNeutralButton("CANCEL", null);

                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });

        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etName.setText(null);
                etTelephone.setText(null);
                etSize.setText(null);
                checkBox.setChecked(false);
                etDate.setText(null);
                etTime.setText(null);

                updatedYear = 0;
                updatedMonth = 0;
                updatedDay = 0;

                updatedHour = 0;
                updatedMinute = 0;

                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().commit();

            }
        });

    }

    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        String name = prefs.getString("name", "");
        boolean smoke = prefs.getBoolean("smoke", false);
        String size = prefs.getString("size", "");
        String date = prefs.getString("date", "");
        String time = prefs.getString("time", "");
        String phone = prefs.getString("phone", "");

        etName.setText(name);
        checkBox.setChecked(smoke);
        etSize.setText(size);
        etDate.setText(date);
        etTime.setText(time);
        etTelephone.setText(phone);

    }
}