package com.example.loginsqllite;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    EditText username, password;
    Button btnlogin;
    DBHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById(R.id.password1);
        btnlogin = (Button) findViewById(R.id.btnsignin1);
        DB = new DBHelper(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if(user.isEmpty() || pass.isEmpty())
                    Toast.makeText(Login.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if(checkuserpass){
                        String userRole=DB.getUserRole(user);
                        if("Farmer".equals(userRole)){
                            Intent farmerIntent=new Intent(getApplicationContext(),MainActivity2.class);
/*
                            farmerIntent.putExtra("username", user);
*/
                            startActivity(farmerIntent);
                        } else if ("Consumer".equals(userRole)) {
                            Intent consumerIntent = new Intent(getApplicationContext(), Consumer.class);
                            consumerIntent.putExtra("username", user);  
                            startActivity(consumerIntent);
                        } else if ("Admin".equals(userRole)) {
                            Intent consumerIntent = new Intent(getApplicationContext(), Admin.class);
                            startActivity(consumerIntent);
                        }
                        else if ("DeliveryBoy".equals(userRole)) {
                            Intent consumerIntent = new Intent(getApplicationContext(), DeliveryBoy.class);
                            startActivity(consumerIntent);
                        }
                        Toast.makeText(Login.this, "Signed in successfully", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}