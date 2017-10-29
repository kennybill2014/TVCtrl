package io.gitub.gtf.tvctrl;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.*;
import android.widget.*;
import android.support.v7.app.*;
import android.provider.*;
import android.content.res.*;
import android.util.*;
import java.util.*;
import io.gitub.gtf.tvctrl.ShellUtils.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
	AlertDialog.Builder askIPAlertDialog ;
	EditText text;
	Button mok;
	Button mup;
	Button mdown;
	Button mleft;
	Button mright;
	Button mback;
	Button moption;
	String ADBdevices;
	SharedPreferences settings;
	SharedPreferences.Editor settingsEditor;
	TextView mIPTextview;

	private ShellUtils.CommandResult result;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		askIPAlertDialog = new AlertDialog.Builder(this);
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		View content_main = View.inflate(getApplicationContext(), R.layout.content_main, null);
		mIPTextview = (TextView)findViewById(R.id.IPTextview);
		mup = (Button)findViewById(R.id.UpButton);
		mdown = (Button)findViewById(R.id.downButton);
		mright = (Button)findViewById(R.id.rightButton);
		mleft = (Button)findViewById(R.id.leftButton);
		mok = (Button)findViewById(R.id.OKButton);
		mback = (Button)findViewById(R.id.backButton);
		moption = (Button)findViewById(R.id.optionButton);
		settings = getSharedPreferences("TVIP", 0);
		String IP = settings.getString("IP", null);
	    settingsEditor  = settings.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
		
		final View askIPAlertDialogView = View.inflate(getApplicationContext(), R.layout.askip_dialog, null);
		Log.w("gtf","66666666666run");
		askIPAlertDialog.setCancelable(false);
		askIPAlertDialog.setView(askIPAlertDialogView);
		askIPAlertDialog.setTitle("请输入电视的IP地址：");
		askIPAlertDialog.setPositiveButton("OK!", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					EditText IPtext = (EditText)askIPAlertDialogView.findViewById(R.id.EditTextdialog);
					IPtext.setRawInputType(Configuration.KEYBOARD_QWERTY);
					String IPAdress = IPtext.getText().toString();
					myWriteLog("Ip dialog bitton clicked ,Ip is:" + IPAdress);
					settingsEditor.putString("IP",IPAdress);
					settingsEditor.commit();
				}
			});
			//askIPAlertDialog.show();
		if (IP == null){
			askIPAlertDialog.show();
			myWriteLog("AskIPDialog had show");
			Log.w("gtf","ii");
		}else{
			myWriteLog("The IP is "+ IP);
			Log.w("gtf",IP);
		}
		mIPTextview.setText("IP: " + IP);
		//String connectADB = runShell("adb connect "+IP);
		//Toast.makeText(MainActivity.this, "Connect to device:/n" + connectADB,Toast.LENGTH_SHORT).show();
		//String ADBdevices = runShell("adb devices");
		//Toast.makeText(MainActivity.this,ADBdevices,Toast.LENGTH_LONG);
		connectDevices();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
	void myWriteLog(String logText){
		WriteLog tempWrite = new WriteLog();
		tempWrite.WriteLog(logText);
	}
	public static String runShell(String 执行内容) {
        Shell.CommandResult 输出值 = Shell.execCommand(执行内容, false, true);
        String 输出 = 输出值.successMsg;
        return 输出;
    }
	public void connectDevices(){
		Toast.makeText(this,"start",Toast.LENGTH_SHORT).show();
		List<String> commnandList = new ArrayList<String>();
		commnandList.add("adb kill-server");
		commnandList.add("adb start-server");
		commnandList.add("adb connect 192.168.31.118");
		//commnandList.add("adb devices");
		//commnandList.add("echo \"185.31.17.184 github.global.ssl.fastly.net\" >> /etc/hosts");
		//commnandList.add("chmod 644 /etc/hosts");
		CommandResult result1 = ShellUtils.execCommand(commnandList, false);
		String a =  result1.errorMsg;
		String b = result1.successMsg;
		Toast.makeText(MainActivity.this,a+" \n " + b ,Toast.LENGTH_LONG).show();
		//String c = result1.result;
	}
	
}
