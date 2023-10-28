package kg.printer.kkm.view;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import kg.printer.kkm.R;
import kg.printer.kkm.controllers.UIViewController;
import kg.printer.kkm.domains.Organization;
import kg.printer.kkm.services.OrganizationService;

public class OrganizationActivity extends UIViewController.BaseAdapter implements View.OnClickListener {

    private Spinner spr_type_of_ownership, spr_taxation;
    private EditText et_org_name, et_inn, et_magazine_name, et_address_magazine, et_telephone_magazine;
    private Button btn_ok;

    private OrganizationService organizationService;
    private Organization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        organizationService = new OrganizationService(this);
        organization = organizationService.readData();

        initView();
        addListener();
        init();
    }

    @Override
    public void initView() {
        ArrayAdapter<String> adapterFormOfSobstvennosti = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organization.getTypesOfOwnership());
        adapterFormOfSobstvennosti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_type_of_ownership = findViewById(R.id.spr_form_of_sobstvennosti);
        spr_type_of_ownership.setAdapter(adapterFormOfSobstvennosti);
        spr_type_of_ownership.setPrompt("Форма собственности");
        spr_type_of_ownership.setSelection(1);
        spr_type_of_ownership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organization.setTypeOfOwnership(spr_type_of_ownership.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ArrayAdapter<String> adapterSystemNalog = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organization.getTaxation());
        adapterSystemNalog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spr_taxation = findViewById(R.id.spr_system_nalog);
        spr_taxation.setAdapter(adapterSystemNalog);
        spr_taxation.setPrompt("Форма собственности");
        spr_taxation.setSelection(0);
        spr_taxation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organization.setTax(spr_taxation.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        et_org_name = findViewById(R.id.et_org_name);
        et_inn = findViewById(R.id.et_inn);
        et_magazine_name = findViewById(R.id.et_magazine_name);
        et_address_magazine = findViewById(R.id.et_adress_magazine);
        et_telephone_magazine = findViewById(R.id.et_telephone_magazine);

        btn_ok = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void init() {
        if (organization.getTypeOfOwnership().equals("Индивидуальный предприниматель")) {
            spr_type_of_ownership.setSelection(0);
        } else {
            spr_type_of_ownership.setSelection(1);
        }

        if (organization.getTax().equals("Общая система")) {
            spr_taxation.setSelection(0);
        } else if (organization.getTax().equals("Упрощенная система")) {
            spr_taxation.setSelection(1);
        }

        et_org_name.setText(organization.getName());
        et_magazine_name.setText(organization.getMagazine_name());
        et_inn.setText(organization.getInn());
        et_address_magazine.setText(organization.getAddress_magazine());
        et_telephone_magazine.setText(organization.getTelephone_magazine());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ok) {
            if (et_org_name.getText().toString().isEmpty()) {
                UIViewController.ToastAdapter.show(this, "Заполните название организации");
            } else if (et_magazine_name.getText().toString().isEmpty()) {
                UIViewController.ToastAdapter.show(this, "Заполните название торговой точки");
            } else if (et_inn.getText().toString().isEmpty() || et_inn.getText().toString().length() < 14) {
                UIViewController.ToastAdapter.show(this, "ИНН должен быть 14 знаков");
            } else {
                String typeOfOwnership = spr_type_of_ownership.getSelectedItem().toString();
                String tax = spr_taxation.getSelectedItem().toString();
                String org_name = et_org_name.getText().toString();
                String inn = et_inn.getText().toString();
                String magazine_name = et_magazine_name.getText().toString();
                String address_magazine = et_address_magazine.getText().toString();
                String telephone_magazine = et_telephone_magazine.getText().toString();

                organizationService.addOrgData(typeOfOwnership, tax, org_name, inn, magazine_name, address_magazine, telephone_magazine);
                hideKeyboard(view);
                finish();
            }
        }
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) OrganizationActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
