package eu.ase.ro.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class PackageHistoryActivity extends AppCompatActivity {

    private ArrayList<ClientPackage> clientPackages;
    private Button btn_inapoi;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_history);

        clientPackages = new ArrayList<>();
        createClientPackages();
        listView = findViewById(R.id.lv_package_history);
        ClientPackageAdapter adapter = new ClientPackageAdapter(getApplicationContext(), R.layout.lv_row_view, clientPackages);
        listView.setAdapter(adapter);

        btn_inapoi = findViewById(R.id.btn_inapoi_istoric);
        btn_inapoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ClientMainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void createClientPackages(){
        ClientPackage clientPackage1 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Emag", 1700.30, 44.419618110257005, 26.109286073162817, 1234, false);
        ClientPackage clientPackage2 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 3200.50, 44.44593407951203, 26.068914698639418, 2345, false);
        ClientPackage clientPackage3 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 2800, 44.4566450172029, 26.094767875858146, 7890, false);
        ClientPackage clientPackage4 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Altex", 4500, 44.428070920384116, 26.090843652424113, 1235, false);
        ClientPackage clientPackage5 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Emag", 260, 44.420249587406545, 26.12895034603028, 1236, false);
        ClientPackage clientPackage6 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Altex", 177.75, 44.48211802267675, 26.09356050062495, 1237, false);
        ClientPackage clientPackage7 = new ClientPackage("o74PXpobHAOI2LMhektehe7tL259", "Televizor", 1500.00);
        ClientPackage clientPackage8 = new ClientPackage("o74PXpobHAOI2LMhektehe7tL259", "PS5", 3000.20);
        ClientPackage clientPackage9 = new ClientPackage("o74PXpobHAOI2LMhektehe7tL259", "Telefon Samsung", 4500.00);
        clientPackages.add(clientPackage1);
        clientPackages.add(clientPackage2);
        clientPackages.add(clientPackage3);
        clientPackages.add(clientPackage4);
        clientPackages.add(clientPackage5);
        clientPackages.add(clientPackage6);
        clientPackages.add(clientPackage7);
        clientPackages.add(clientPackage8);
        clientPackages.add(clientPackage9);
    }
}