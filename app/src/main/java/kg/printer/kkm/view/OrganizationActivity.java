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

    private Spinner sprTypeOfOwnership, sprTaxation;
    private EditText etOrgName, etInn, etMagazineName, etAddressMagazine, etTelephoneMagazine;
    private Button btnOk;

    private OrganizationService organizationService;

    private Organization organization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        init();
        initView();
        addListener();

        updateView();
    }

    @Override
    public void init() {
        organizationService = new OrganizationService(this);
        organization = organizationService.readOrganization();
    }

    @Override
    public void initView() {
        ArrayAdapter<String> adapterFormOfSobstvennosti = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organization.getTypesOfOwnership());
        adapterFormOfSobstvennosti.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprTypeOfOwnership = findViewById(R.id.spr_type_of_ownership);
        sprTypeOfOwnership.setAdapter(adapterFormOfSobstvennosti);
        sprTypeOfOwnership.setPrompt("Форма собственности");
        sprTypeOfOwnership.setSelection(1);
        sprTypeOfOwnership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organization.setTypeOfOwnership(sprTypeOfOwnership.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        ArrayAdapter<String> adapterSystemNalog = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, organization.getTaxationList());
        adapterSystemNalog.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sprTaxation = findViewById(R.id.spr_taxation);
        sprTaxation.setAdapter(adapterSystemNalog);
        sprTaxation.setPrompt("Форма собственности");
        sprTaxation.setSelection(0);
        sprTaxation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                organization.setTaxation(sprTaxation.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        etOrgName = findViewById(R.id.et_org_name);
        etInn = findViewById(R.id.et_inn);
        etMagazineName = findViewById(R.id.et_magazine_name);
        etAddressMagazine = findViewById(R.id.et_magazine_address);
        etTelephoneMagazine = findViewById(R.id.et_magazine_telephone);

        btnOk = findViewById(R.id.btn_ok);
    }

    @Override
    public void addListener() {
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ok) {
            if (etOrgName.getText().toString().isEmpty()) {
                showToast("Заполните название организации");
            } else if (etMagazineName.getText().toString().isEmpty()) {
                showToast("Заполните название торговой точки");
            } else if (etInn.getText().toString().isEmpty() || etInn.getText().toString().length() < 14) {
                showToast("ИНН должен быть 14 знаков");
            } else {
                Organization organization = new Organization();
                organization.setTypeOfOwnership(sprTypeOfOwnership.getSelectedItem().toString());
                organization.setTaxation(sprTaxation.getSelectedItem().toString());
                organization.setName(etOrgName.getText().toString());
                organization.setInn(etInn.getText().toString());
                organization.setMagazineName(etMagazineName.getText().toString());
                organization.setMagazineAddress(etAddressMagazine.getText().toString());
                organization.setMagazineTelephone(etTelephoneMagazine.getText().toString());

                organizationService.updateOrganization(organization);
                hideKeyboard(view);
                finish();
            }
        }
    }

    private void updateView() {
        if (organization.getTypeOfOwnership().equals("Индивидуальный предприниматель")) {
            sprTypeOfOwnership.setSelection(0);
        } else {
            sprTypeOfOwnership.setSelection(1);
        }

        if (organization.getTaxation().equals("Общая система")) {
            sprTaxation.setSelection(0);
        } else if (organization.getTaxation().equals("Упрощенная система")) {
            sprTaxation.setSelection(1);
        }

        etOrgName.setText(organization.getName());
        etMagazineName.setText(organization.getMagazineName());
        etInn.setText(organization.getInn());
        etAddressMagazine.setText(organization.getMagazineAddress());
        etTelephoneMagazine.setText(organization.getMagazineTelephone());
    }

    private void hideKeyboard(View view) {
        View viewLayout = this.getCurrentFocus();
        if (viewLayout != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) OrganizationActivity.this.getSystemService(UIViewController.BaseAdapter.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
