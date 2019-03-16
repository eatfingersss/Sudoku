
package top.eatfingersss.sudoku.view.activity.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.*;
import android.support.annotation.Nullable;
import top.eatfingersss.sudoku.R;
import top.eatfingersss.sudoku.control.PublicInformation;

import top.eatfingersss.sudoku.view.MessageBox;

public class SettingsActivity extends PreferenceActivity {
    @SuppressLint("ResourceType")
    private Preference block;
    private SwitchPreference switchPreferenceDeveloperMode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        colorPickerViewOnCreate();
//        addContentView();
//        setContentView(R.layout.activity_settings);
        try {
            addPreferencesFromResource(R.xml.activity_settings);
        }
        catch (Exception e){
            MessageBox.showMessage(this,"选项页面无法加载："+e.getMessage());
            return;
        }
        nodeGet();
        listennerAdd();


//        PreferenceScreen preferenceScreen = (PreferenceScreen) findPreference()

    }


    public void nodeGet(){
        switchPreferenceDeveloperMode=
                (SwitchPreference)findPreference(
                        getApplicationContext().getString(
                                R.string.settings_switch_preference_developer_mode_key
                        )
                );
        switchPreferenceDeveloperMode.
                setChecked(PublicInformation.developerMode);//开发者模式默认值
    }

    private void listennerAdd(){
        switchPreferenceDeveloperMode.
                setOnPreferenceChangeListener(new SwitchPreferenceOnChanged());
    }

    class SwitchPreferenceOnChanged implements SwitchPreference.OnPreferenceChangeListener{
        //值发生改变，需要手动存
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            SwitchPreference target = (SwitchPreference) preference;
            target.setChecked((boolean) newValue);
            PublicInformation.developerMode=(boolean) newValue;
            return (boolean) newValue;
        }
    }

    /**
     * 刷新用户信息
     */

//    private void flushUserInfo() {
//        String pref = findPreference(R.string.pref_key_user_info)
//        String user = UserHolder.getUser()
//        if (user == null) {
//            findPreference(R.string.pref_key_user_logout).isEnabled = false
//            if (NetState.state.value > 0) {
//                pref.title = "未登录"
//                pref.summary = "点击登录"
//                pref.intent = Intent(activity, LoginActivity::class.java)
//            } else {
//                pref.title = "网络连接失败"
//                pref.summary = "点击打开网络连接"
//                pref.intent = Intent(Settings.ACTION_WIFI_SETTINGS)
//            }
//        } else {
//            findPreference(R.string.pref_key_user_logout).isEnabled = true
//            pref.title = "您好 陈小默"
//            pref.summary = "您当前的积分:${user.score}"
//            pref.intent = null
//        }
//    }

//    //似乎是功能需要的东西
//    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object value) {
//            String stringValue = value.toString();
//
//            if (preference instanceof ListPreference) {
//                // For list preferences, look up the correct display value in
//                // the preference's 'entries' list.
//                ListPreference listPreference = (ListPreference) preference;
//                int index = listPreference.findIndexOfValue(stringValue);
//
//                // Set the summary to reflect the new value.
//                preference.setSummary(
//                        index >= 0
//                                ? listPreference.getEntries()[index]
//                                : null);
//
//            } else if (preference instanceof RingtonePreference) {
//                // For ringtone preferences, look up the correct display value
//                // using RingtoneManager.
//                if (TextUtils.isEmpty(stringValue)) {
//                    // Empty values correspond to 'silent' (no ringtone).
//                    preference.setSummary(R.string.pref_ringtone_silent);
//
//                } else {
//                    Ringtone ringtone = RingtoneManager.getRingtone(
//                            preference.getContext(), Uri.parse(stringValue));
//
//                    if (ringtone == null) {
//                        // Clear the summary if there was a lookup error.
//                        preference.setSummary(null);
//                    } else {
//                        // Set the summary to reflect the new ringtone display
//                        // name.
//                        String name = ringtone.getTitle(preference.getContext());
//                        preference.setSummary(name);
//                    }
//                }
//
//            } else {
//                // For all other preferences, set the summary to the value's
//                // simple string representation.
//                preference.setSummary(stringValue);
//            }
//            return true;
//        }
//    };
//
//    /**
//     * Helper method to determine if the device has an extra-large screen. For
//     * example, 10" tablets are extra-large.
//     */
//    private static boolean isXLargeTablet(Context context) {
//        return (context.getResources().getConfiguration().screenLayout
//                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
//    }
//
//    /**
//     * Binds a preference's summary to its value. More specifically, when the
//     * preference's value is changed, its summary (line of text below the
//     * preference title) is updated to reflect the value. The summary is also
//     * immediately updated upon calling this method. The exact display format is
//     * dependent on the type of preference.
//     *
//     * @see #sBindPreferenceSummaryToValueListener
//     */
//    private static void bindPreferenceSummaryToValue(Preference preference) {
//        // Set the listener to watch for value changes.
//        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
//
//        // Trigger the listener immediately with the preference's
//        // current value.
//        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
//    }
//
//
////    /**
////     * Set up the {@link android.app.ActionBar}, if the API is available.
////     */
////    private void setupActionBar() {
////        ActionBar actionBar = getSupportActionBar();
////        if (actionBar != null) {
////            // Show the Up button in the action bar.
////            actionBar.setDisplayHomeAsUpEnabled(true);
////        }
////    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean onIsMultiPane() {
//        return isXLargeTablet(this);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public void onBuildHeaders(List<Header> target) {
//        loadHeadersFromResource(R.xml.pref_headers, target);
//    }
//
//    /**
//     * This method stops fragment injection in malicious applications.
//     * Make sure to deny any unknown fragments here.
//     */
//    protected boolean isValidFragment(String fragmentName) {
//        return PreferenceFragment.class.getName().equals(fragmentName)
//                || top.eatfingersss.sudoku.view.activity.activity_settings.SettingsActivity.ColorPickerPreferenceFragment.class.getName().equals(fragmentName);
//    }
//
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    public static class ColorPickerPreferenceFragment extends PreferenceFragment {
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.layout.activity_color_picker);
//            setHasOptionsMenu(true);
//
//            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
//            // to their values. When their values change, their summaries are
//            // updated to reflect the new value, per the Android Design
//            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("activity_color_picker"));
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id = item.getItemId();
//            if (id == android.R.id.home) {
//                startActivity(
//                        new Intent(getActivity(),
//                            top.eatfingersss.sudoku.view.activity.activity_settings.SettingsActivity.class));
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }
//    }


}
