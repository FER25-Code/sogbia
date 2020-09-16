package com.sci.sogbia.SendEmail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sci.sogbia.R;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import androidx.appcompat.app.AppCompatActivity;

public class SendEmailActivity extends AppCompatActivity {
    EditText emailtext , password;
    TextView emailShow;
    Session session = null;
    ProgressDialog pdialog = null;
    String rec, subject, textMessage;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_email_send_email);
        emailtext=findViewById(R.id.idemailSend);
        password=findViewById(R.id.idemailpassword);
        emailShow=findViewById(R.id.showemail);
        Intent send = getIntent();
        Bundle bundle =send .getExtras();
      bundle.getString("email");
        emailShow.setText(bundle.getString("email"));
    }
    public void Send(View view) {
        final String pass = password.getText().toString();
        if (pass.isEmpty()){
            Toast.makeText(getApplicationContext(), "password vide ", Toast.LENGTH_LONG).show();
        }else {
            email = emailShow.getText().toString();
            rec = "ramy008.rf@gmail.com";
            subject = "Réclamation";
            textMessage = emailtext.getText().toString();
            Log.e("NO email", " ccccccccccc");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, pass);
                }
            });
            pdialog = ProgressDialog.show(this, "", "Sending Mail...", true);
            RetreiveFeedTask task = new RetreiveFeedTask();
            task.execute();
        }
    }
    public class RetreiveFeedTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params  ) {
            try{
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(email));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
                message.setSubject(subject);
                message.setContent(textMessage, "text/html; charset=utf-8");
                Transport.send(message);
            } catch(MessagingException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(), "Message sent", Toast.LENGTH_LONG).show();
        }
    }


}
