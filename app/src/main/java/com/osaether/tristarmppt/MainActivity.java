package com.osaether.tristarmppt;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import com.osaether.modbus.ModbusTCP;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String version = BuildConfig.VERSION_NAME;
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            TristarMPPTFragment fragment = new TristarMPPTFragment();
            transaction.replace(R.id.content_fragment, fragment);
            transaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Dialog about = new Dialog(this);
                about.setContentView(R.layout.about);
                about.setTitle(R.string.about_title);
                about.show();
                break;
            case R.id.menu_show_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, 0);
                break;
            case R.id.menu_refresh:
                ModbusAccess ma = new ModbusAccess(this.getApplicationContext());
                String dummy = new String("");
                ma.execute();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    class ModbusAccess extends AsyncTask<Void, Void, TristarData> {
        private Context m_context;
        private Exception exception;
        private String m_host;
        private int m_port;
        private int m_slave_id;

        private TristarData m_tristarData = new TristarData();

        public ModbusAccess(Context context) {
            this.m_context = context.getApplicationContext();
        }

        private void ReadSettings() {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(m_context);

            String defHost = m_context.getString(R.string.pref_default_ip_address);
            String defPort = m_context.getString(R.string.pref_default_port);
            String defSlaveId = m_context.getString(R.string.pref_default_slave_id);

            this.m_host = sharedPrefs.getString("ip_address", defHost);
            String port = sharedPrefs.getString("ip_port", defPort);
            this.m_port = Integer.parseInt(port, 10);
            String slave_id = sharedPrefs.getString("slave_id", defSlaveId);
            this.m_slave_id = Integer.parseInt(slave_id, 10);
            this.m_tristarData.m_fahrenheit = !sharedPrefs.getBoolean("use_celsius", true);;
        }

        protected TristarData doInBackground(Void... dummy) {
            try {
                ReadSettings();
                m_tristarData.m_berror = false;

                ModbusTCP modbus = new ModbusTCP(this.m_host, this.m_port);
                modbus.connect();
                short[] tsdata = modbus.readInputRegisters(0, 80);
                modbus.close();

                m_tristarData.m_v_pu = (float)tsdata[0];
                m_tristarData.m_i_pu = (float)tsdata[2];
                m_tristarData.m_vbat = m_tristarData.m_v_pu * (float)tsdata[24] / 32768.0f;
                m_tristarData.m_vtarget = m_tristarData.m_v_pu * (float)tsdata[51] / 32768.0f;
                m_tristarData.m_icharge = m_tristarData.m_i_pu * (float)tsdata[28] / 32768.0f;
                m_tristarData.m_chargestate = tsdata[50];
                m_tristarData.m_pout = m_tristarData.m_i_pu * m_tristarData.m_v_pu * (float)tsdata[58] / 131072.0f;
                m_tristarData.m_varray = m_tristarData.m_v_pu * (float)tsdata[27] / 32768.0f;
                m_tristarData.m_iarray = m_tristarData.m_i_pu * (float)tsdata[29] / 32768.0f;
                m_tristarData.m_vmp = m_tristarData.m_v_pu * (float)tsdata[61] / 32768.0f;
                m_tristarData.m_voc = m_tristarData.m_v_pu * (float)tsdata[62] / 32768.0f;
                m_tristarData.m_pvvmaxdaily = m_tristarData.m_v_pu * (float)tsdata[66] / 32768.0f;
                m_tristarData.m_spmax = m_tristarData.m_i_pu *m_tristarData.m_v_pu * (float)tsdata[60] / 131072.0f;
                m_tristarData.m_pmaxdaily = m_tristarData.m_i_pu *m_tristarData.m_v_pu * (float)tsdata[70] / 131072.0f;
                // m_tristarData.m_pmaxdaily = (float)temp/10.0f;
                m_tristarData.m_vmin = m_tristarData.m_v_pu * (float)tsdata[64] / 32768.0f;
                m_tristarData.m_vmax = m_tristarData.m_v_pu * (float)tsdata[65] / 32768.0f;

                // Counters:
                long temph = (long)tsdata[52];
                temph = temph & 0xffffL;
                long templ = (long)tsdata[53];
                templ = templ & 0xffffL;
                templ = templ + temph * 65536;
                m_tristarData.m_ahres = (float)templ * 0.1f;

                temph = (long)tsdata[54];
                temph = temph & 0xffffL;
                templ = (long)tsdata[55];
                templ = templ & 0xffffL;
                templ = templ + temph * 65536;
                m_tristarData.m_ahtot = (float)templ * 0.1f;

                m_tristarData.m_ahdaily = (float)tsdata[67] * 0.1f;
                m_tristarData.m_kwhres = (float)tsdata[56];
                m_tristarData.m_kwhtot = (float)tsdata[57];
                m_tristarData.m_whdaily = (float)tsdata[68];
                // Temperatures
//                m_tristarData.m_thsink = read_signed_short_input_register(tcpMaster, 35);
//                m_tristarData.m_tbat = read_signed_short_input_register(tcpMaster, 37);
//                m_tristarData.m_tbatmin = read_signed_short_input_register(tcpMaster, 71);
//                m_tristarData.m_tbatmax = read_signed_short_input_register(tcpMaster, 72);
                m_tristarData.m_thsink = tsdata[35];
                m_tristarData.m_tbat = tsdata[37];
                m_tristarData.m_tbatmin = tsdata[71];
                m_tristarData.m_tbatmax = tsdata[72];
                if (m_tristarData.m_fahrenheit)
                {
                    m_tristarData.m_thsink = (short)((double)m_tristarData.m_thsink * 9.0/5.0 + 32.0);
                    m_tristarData.m_tbat = (short)((double)m_tristarData.m_tbat * 9.0/5.0 + 32.0);
                    m_tristarData.m_tbatmin = (short)((double)m_tristarData.m_tbatmin * 9.0/5.0 + 32.0);
                    m_tristarData.m_tbatmax = (short)((double)m_tristarData.m_tbatmax * 9.0/5.0 + 32.0);
                }

                m_tristarData.m_timeequalize = tsdata[78];
                m_tristarData.m_timefloat = tsdata[79];
                m_tristarData.m_timeabsorption = tsdata[77];

                return m_tristarData;
            }
            catch (Exception e) {
                Log.e(getClass().getSimpleName(), e.toString());
                m_tristarData.m_berror = true;
                m_tristarData.error = e.toString();
                return m_tristarData;
            }
        }

        protected void onPostExecute(TristarData result) {
            if (!m_tristarData.m_berror) {
                TristarMPPTFragment fragment = (TristarMPPTFragment) getSupportFragmentManager().findFragmentById(R.id.content_fragment);
                fragment.setTristarData(result);
            } else {
                Toast.makeText(m_context, result.error, Toast.LENGTH_LONG).show();
            }

        }
    }
}
