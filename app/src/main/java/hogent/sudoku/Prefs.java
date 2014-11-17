package hogent.sudoku;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by jbuy519 on 14/08/2014.
 */
public class Prefs extends PreferenceFragment{

    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
