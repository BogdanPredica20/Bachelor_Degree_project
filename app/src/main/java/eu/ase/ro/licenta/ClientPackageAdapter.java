package eu.ase.ro.licenta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ClientPackageAdapter extends ArrayAdapter<ClientPackage> {

    private Context context;
    private int resource;
    private List<ClientPackage> clientPackages;

    public ClientPackageAdapter(@NonNull Context context, int resource, @NonNull List<ClientPackage> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.clientPackages = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       if(convertView == null) {
           convertView = LayoutInflater.from(getContext()).inflate(R.layout.lv_row_view,
                   parent, false);
       }
        ClientPackage clientPackage = clientPackages.get(position);
        if(clientPackage == null) {
            return convertView;
        }
        addIdCurier(convertView, clientPackage.getId_curier());
        addColet(convertView, clientPackage.getColet());
        addSuma(convertView, clientPackage.getSuma());
        addPin(convertView, clientPackage.getPin());
        return convertView;
    }

    private void addPin(View convertView, int pin) {
        TextView textView = convertView.findViewById(R.id.row_tv_pin);
        if(String.valueOf(pin) != null){
            textView.append(String.valueOf(pin));
        } else {
            textView.setText(R.string.client_package_adapter_default_value);
        }
    }

    private void addSuma(View view, double suma) {
        TextView textView = view.findViewById(R.id.row_tv_suma);
        if(String.valueOf(suma) != null){
            textView.setText(String.valueOf(suma));
            textView.append(" RON");
        } else {
            textView.setText(R.string.client_package_adapter_default_value);
        }
    }

    private void addColet(View view, String colet) {
        TextView textView = view.findViewById(R.id.row_tv_colet);
        if(colet != null){
            textView.setText(colet);
        } else {
            textView.setText(R.string.client_package_adapter_default_value);
        }
    }

    private void addIdCurier(View view, String id_curier) {
        TextView textView = view.findViewById(R.id.row_tv_courier_id);
        if(id_curier != null){
            textView.setText(id_curier);
        } else {
            textView.setText(R.string.client_package_adapter_default_value);
        }
    }
}
