package com.example.studentflowensa;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studentflowensa.model.Etudiant;
import com.example.studentflowensa.service.EtudiantService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText inputNom;
    private TextInputEditText inputPrenom;
    private TextInputEditText inputId;

    private MaterialButton btnAjouter;
    private MaterialButton btnChercher;
    private MaterialButton btnSupprimer;

    private TextView tvResultat;

    private EtudiantService etudiantService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etudiantService = new EtudiantService(this);

        inputNom = findViewById(R.id.inputNom);
        inputPrenom = findViewById(R.id.inputPrenom);
        inputId = findViewById(R.id.inputId);

        btnAjouter = findViewById(R.id.btnAjouter);
        btnChercher = findViewById(R.id.btnChercher);
        btnSupprimer = findViewById(R.id.btnSupprimer);

        tvResultat = findViewById(R.id.tvResultat);

        btnAjouter.setOnClickListener(v -> ajouterEtudiant());
        btnChercher.setOnClickListener(v -> chercherEtudiant());
        btnSupprimer.setOnClickListener(v -> supprimerEtudiant());
    }

    private void ajouterEtudiant() {
        String nom = inputNom.getText() != null ? inputNom.getText().toString().trim() : "";
        String prenom = inputPrenom.getText() != null ? inputPrenom.getText().toString().trim() : "";

        if (TextUtils.isEmpty(nom) || TextUtils.isEmpty(prenom)) {
            Toast.makeText(this, "Veuillez remplir le nom et le prénom", Toast.LENGTH_SHORT).show();
            return;
        }

        long insertedId = etudiantService.create(new Etudiant(nom, prenom));

        inputNom.setText("");
        inputPrenom.setText("");

        tvResultat.setText("Étudiant ajouté avec succès.\nID généré : " + insertedId);
        Toast.makeText(this, "Ajout effectué", Toast.LENGTH_SHORT).show();
    }

    private void chercherEtudiant() {
        String idText = inputId.getText() != null ? inputId.getText().toString().trim() : "";

        if (TextUtils.isEmpty(idText)) {
            Toast.makeText(this, "Veuillez saisir un identifiant", Toast.LENGTH_SHORT).show();
            return;
        }

        Etudiant e = etudiantService.findById(Integer.parseInt(idText));

        if (e == null) {
            tvResultat.setText("Aucun étudiant trouvé pour cet identifiant.");
            return;
        }

        tvResultat.setText(
                "Résultat de recherche\n\n" +
                        "ID : " + e.getId() + "\n" +
                        "Nom : " + e.getNom() + "\n" +
                        "Prénom : " + e.getPrenom()
        );
    }

    private void supprimerEtudiant() {
        String idText = inputId.getText() != null ? inputId.getText().toString().trim() : "";

        if (TextUtils.isEmpty(idText)) {
            Toast.makeText(this, "Veuillez saisir un identifiant", Toast.LENGTH_SHORT).show();
            return;
        }

        Etudiant e = etudiantService.findById(Integer.parseInt(idText));

        if (e == null) {
            tvResultat.setText("Suppression impossible : étudiant introuvable.");
            return;
        }

        etudiantService.delete(e);
        tvResultat.setText("L'étudiant avec l'ID " + e.getId() + " a été supprimé avec succès.");
        Toast.makeText(this, "Suppression effectuée", Toast.LENGTH_SHORT).show();
    }
}