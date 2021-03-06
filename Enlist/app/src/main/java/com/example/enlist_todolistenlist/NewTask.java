package com.example.enlist_todolistenlist;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class NewTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "NewTask";
    RadioGroup radioGroup_privacy;
    RadioButton radioButton_personal,radioButton_shared;
    int type_privacy=0;
    Vibrator vibrator;

    String currentDateString,temp_month;
    String kkk;
    String[] splited;

    EditText title_editText, description_editText;
    TextView deadline_textView;
    ImageButton date_picker_btn, back_arrow_btn, mic1, mic2;
    FloatingActionButton add_task_btn,add_to_calendar_btn;
    Integer qqq, i,flagg=0;

    DatabaseReference reference, users_reference;
    Integer taskNumber = new Random().nextInt();
    String key_layout = Integer.toString(taskNumber);

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewTask.this);
        builder.setMessage("Discard the current task?").setCancelable(false).setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(NewTask.this,MainActivity.class));
                finish();
            }
        }).setNegativeButton("No, wait", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        title_editText = findViewById(R.id.title_editText);
        description_editText = findViewById(R.id.description_editText);
        deadline_textView = findViewById(R.id.deadline_textView);
        back_arrow_btn = findViewById(R.id.back_arrow_btn);
        mic1 = findViewById(R.id.mic_title);
        mic2 = findViewById(R.id.mic_description);
        radioGroup_privacy = findViewById(R.id.radioGroup_privacy);
        radioButton_personal = findViewById(R.id.radioButton_personal);
        radioButton_shared = findViewById(R.id.radioButton_shared);
        add_to_calendar_btn = findViewById(R.id.add_to_calendar);
        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        date_picker_btn = findViewById(R.id.open_picker);
        add_task_btn = findViewById(R.id.add_task_btn);

        radioGroup_privacy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButton_personal:
                        type_privacy=1;
                        break;
                    case R.id.radioButton_shared:
                        type_privacy=2;
                        break;
                }
            }
        });

        title_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(title_editText.getText().toString().length() == 21){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(title_editText.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        description_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(description_editText.getText().toString().length() == 35){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(description_editText.getWindowToken(), 0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Enter title");
                try {
                    startActivityForResult(intent,1);
                }catch (ActivityNotFoundException e){
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        mic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Enter description");
                try {
                    startActivityForResult(intent,2);
                }catch (ActivityNotFoundException e){
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }
            }
        });

        back_arrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NewTask.this);
                builder.setMessage("Discard the current task?").setCancelable(false).setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(NewTask.this,MainActivity.class));
                        finish();
                    }
                }).setNegativeButton("No, wait", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        date_picker_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                DialogFragment datePicker = new DataPickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        add_to_calendar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(title_editText.getText().toString())) {
                    Toast.makeText(NewTask.this, "Enter title", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(description_editText.getText().toString())) {
                    description_editText = null;
                }else{
                    Intent intent_task = new Intent(Intent.ACTION_INSERT);
                    intent_task.setData(CalendarContract.Events.CONTENT_URI);
                    intent_task.putExtra(CalendarContract.Events.TITLE, title_editText.getText().toString());
                    intent_task.putExtra(CalendarContract.Events.DESCRIPTION, description_editText.getText().toString());
                    intent_task.putExtra(CalendarContract.Events.ALL_DAY, true);

                    if(intent_task.resolveActivity(getPackageManager()) != null){
                        startActivity(intent_task);
                    }
                    else{
                        Toast.makeText(NewTask.this, "No app found to support this action", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        add_task_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(title_editText.getText().toString())) {
                    Toast.makeText(NewTask.this, "Enter title", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(currentDateString)) {
                    Toast.makeText(NewTask.this, "Enter deadline", Toast.LENGTH_SHORT).show();
                } else if(type_privacy == 0){
                    Toast.makeText(NewTask.this, "Choose privacy type", Toast.LENGTH_SHORT).show();
                }
                else {

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format_time = new SimpleDateFormat("hh:mm:ss");
                    SimpleDateFormat format_date = new SimpleDateFormat("dd MMM yyyy");
                    String time = format_time.format(calendar.getTime());
                    String date = format_date.format(calendar.getTime());

                    Map<String, Object> note = new HashMap<>();
                    note.put("date",date);
                    note.put("title",title_editText.getText().toString());
                    note.put("name",Source.main_user_name);

                    db.collection("Analysis").document(time).set(note)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: ");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: ");
                            Log.d(TAG,e.toString());
                        }
                    });

                    if(type_privacy == 1){
                        reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(Source.main_user_uid).child(Source.main_class_group).child("Task" + taskNumber);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                snapshot.getRef().child("title").setValue(title_editText.getText().toString());
                                if (TextUtils.isEmpty(description_editText.getText().toString())) {
                                    description_editText.setText(" ");
                                }else{
                                    snapshot.getRef().child("description").setValue(description_editText.getText().toString());
                                }
                                snapshot.getRef().child("description").setValue(description_editText.getText().toString());
                                snapshot.getRef().child("deadline").setValue(currentDateString);
                                snapshot.getRef().child("key").setValue(key_layout);
                                vibrator.vibrate(30);
                                startActivity(new Intent(NewTask.this,MainActivity.class));
                                finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.d(TAG, error.getMessage());
                            }
                        });
                    }
                    else if(type_privacy == 2){
                        users_reference = FirebaseDatabase.getInstance().getReference().child("Source").child(Source.main_class_group);

                        users_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                kkk = "";
                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    kkk += snapshot1.getKey() + " ";
                                }
                                splited = kkk.split("\\s+");
                                qqq = splited.length;

                                for (i = 0; i < splited.length; i++) {

                                    reference = FirebaseDatabase.getInstance().getReference().child("To-Do-List").child(splited[i]).child(Source.main_class_group).child("Task" + taskNumber);

                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.getRef().child("title").setValue(title_editText.getText().toString());
                                            if (TextUtils.isEmpty(description_editText.getText().toString())) {
                                                description_editText.setText(" ");
                                            }else{
                                                snapshot.getRef().child("description").setValue(description_editText.getText().toString());
                                            }
                                            snapshot.getRef().child("deadline").setValue(currentDateString);
                                            snapshot.getRef().child("key").setValue(key_layout);
                                           flagg++;
                                            if (i.equals(qqq) && flagg.equals(qqq)) {
                                                vibrator.vibrate(30);
                                                startActivity(new Intent(NewTask.this, MainActivity.class));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Log.d(TAG, "Error");
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }
        });
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        month+=1;
        switch (month){
            case 1:{
                temp_month="Jan";
                break;
            }
            case 2:{
                temp_month="Feb";
                break;
            }
            case 3:{
                temp_month="Mar";
                break;
            }
            case 4:{
                temp_month="Apr";
                break;
            }
            case 5:{
                temp_month="May";
                break;
            }
            case 6:{
                temp_month="Jun";
                break;
            }
            case 7:{
                temp_month="Jul";
                break;
            }
            case 8:{
                temp_month="Aug";
                break;
            }
            case 9:{
                temp_month="Sep";
                break;
            }
            case 10:{
                temp_month="Oct";
                break;
            }
            case 11:{
                temp_month="Nov";
                break;
            }
            case 12:{
                temp_month="Dec";
                break;
            }
        }
        if(day == 1 || day == 2 || day == 3 || day == 4 || day == 5 || day == 6 || day == 7 || day == 8 || day == 9){
            currentDateString = "0" + day + " " + temp_month;
        }
        else{
            currentDateString = day + " " + temp_month;
        }
        deadline_textView.setText(currentDateString);
    }

    private void closeKeyboard() {
        View view = NewTask.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null){
                        title_editText.setText(result.get(0));
                    }
                }
                break;
            case 2:
                if(resultCode == RESULT_OK && null != data){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (result != null){
                        description_editText.setText(result.get(0));
                    }
                }
                break;
        }
    }
}