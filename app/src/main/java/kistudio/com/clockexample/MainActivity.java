package kistudio.com.clockexample;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String LOG_TAG = "clockApps";
    Context context;
    TextView tv;
    RadioButton rb1,rb2,rb3;
    RadioGroup r1;
    FrameLayout fl1;
    LinearLayout l1;
    Timer timer;
    Date result;
    ViewGroup.LayoutParams lp1;
    Drawable bck;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        tv = (TextView)findViewById(R.id.TextView01);
        r1=(RadioGroup)findViewById(R.id.RadioGroup01);
        rb1=(RadioButton)findViewById(R.id.RadioButton01);
        rb2=(RadioButton)findViewById(R.id.RadioButton02);
        rb3=(RadioButton)findViewById(R.id.RadioButton03);
        fl1=(FrameLayout)findViewById(R.id.FrameLayout01);
        //bck=this.getResources().getDrawable(R.drawable.clockface);
        //fl1.setBackgroundDrawable(bck);
        lp1=fl1.getLayoutParams();
        r1.setOnCheckedChangeListener(this);
        result=null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(rb1.isChecked() == true)
            updateTime("Asia/Kolkata");
        if(rb2.isChecked() == true)
            updateTime("Etc/GMT-6");
        if(rb3.isChecked() == true)
            updateTime("Asia/Karachi");
    }

    private void updateTime(String str) {
        // TODO Auto-generated method stub
        if(timer!=null)
        {
            timer.cancel();
            timer=null;
        }
        timer = new Timer();
        MyTime mt=new MyTime(this,str);
        timer.schedule(mt, 1, 1000);
    }

    public class MyTime extends TimerTask {
        String tz;
        public MyTime(Context context,String str) {
            tz=str;
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Log.d(LOG_TAG,"Run");
            try{
                Date date=new Date();
                date=getDateInTimeZone(date, tz);

                //System.out.println(date.toLocaleString());
                result=date;
                handler.sendEmptyMessage(0);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        private Date getDateInTimeZone(Date currentDate, String timeZoneId) {
            TimeZone tz = TimeZone.getTimeZone(timeZoneId);
            Calendar mbCal = new GregorianCalendar(tz);
            mbCal.setTimeInMillis(currentDate.getTime());
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, mbCal.get(Calendar.YEAR));
            cal.set(Calendar.MONTH, mbCal.get(Calendar.MONTH));
            cal.set(Calendar.DAY_OF_MONTH, mbCal.get(Calendar.DAY_OF_MONTH));
            cal.set(Calendar.HOUR_OF_DAY, mbCal.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, mbCal.get(Calendar.MINUTE));
            cal.set(Calendar.SECOND, mbCal.get(Calendar.SECOND));
            cal.set(Calendar.MILLISECOND, mbCal.get(Calendar.MILLISECOND));


            return cal.getTime();
        }
    }
    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            tv.setText(result.toLocaleString());
            //System.out.println(result.getHours()+" "+result.getMinutes());
            fl1.removeAllViews();
            fl1.addView(new CustomClock(context, lp1.height/2, lp1.width/2, result));
        }
    };
}
