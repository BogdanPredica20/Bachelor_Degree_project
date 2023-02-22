package eu.ase.ro.licenta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ClientMainActivity extends AppCompatActivity {

    private ArrayList<ClientPackage> clientPackages;
    private Button btn_logout;
    private Button btn_istoric;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);

        initializareVariabile();
        openMapActivity();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ClientMainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_istoric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PackageHistoryActivity.class);
                startActivity(intent);
            }
        });

        clientPackages = new ArrayList<>();
        createClientPackages();

        ClientPackageAdapter adapter = new ClientPackageAdapter(getApplicationContext(), R.layout.lv_row_view, clientPackages);
        listView.setAdapter(adapter);
    }

    public void createClientPackages(){
        ClientPackage clientPackage1 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Emag", 1700.30, 44.419618110257005, 26.109286073162817, 1234, false);
        ClientPackage clientPackage2 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 3200.50, 44.44593407951203, 26.068914698639418, 2345, false);
        ClientPackage clientPackage3 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet PcGarage", 2800, 44.4566450172029, 26.094767875858146, 7890, false);
        ClientPackage clientPackage4 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Altex", 4500, 44.428070920384116, 26.090843652424113, 1235, false);
        ClientPackage clientPackage5 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Emag", 260, 44.420249587406545, 26.12895034603028, 1236, false);
        ClientPackage clientPackage6 = new ClientPackage("y74JQtvbHAOI8KZhektehe7tL472", "Colet Altex", 177.75, 44.48211802267675, 26.09356050062495, 1237, false);
        clientPackages.add(clientPackage1);
        clientPackages.add(clientPackage2);
        clientPackages.add(clientPackage3);
        clientPackages.add(clientPackage4);
        clientPackages.add(clientPackage5);
        clientPackages.add(clientPackage6);
    }

    private void initializareVariabile() {
        btn_logout = findViewById(R.id.btn_logout_client);
        btn_istoric = findViewById(R.id.btn_istoric_colete);
        listView = findViewById(R.id.lv_packages);
    }

    private void openMapActivity() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String curierId = ((TextView)view.findViewById(R.id.row_tv_courier_id)).getText().toString();
                Intent intent = new Intent(getApplicationContext(), ClientMapActivity.class);
                intent.putExtra("idCurier", curierId);
                startActivity(intent);
            }
        });
    }
}