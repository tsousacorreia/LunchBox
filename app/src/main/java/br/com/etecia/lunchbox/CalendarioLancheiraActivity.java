package br.com.etecia.lunchbox;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Calendar;

public class CalendarioLancheiraActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_lancheira);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab_Layout_calendar);

        // Obter o número de dias no mês atual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Janeiro é 0, Dezembro é 11
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Número de dias no mês

        // Configure o adapter para o ViewPager2
        CalendarioPagerAdapter adapter = new CalendarioPagerAdapter(this, numDays, year, month);
        viewPager.setAdapter(adapter);

        // Vincule o TabLayout ao ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("Dia " + (position + 1))
        ).attach();
    }
}