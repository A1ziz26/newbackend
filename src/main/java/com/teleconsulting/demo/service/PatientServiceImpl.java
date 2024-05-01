package com.teleconsulting.demo.service;

import com.teleconsulting.demo.dto.Pdetails;
import com.teleconsulting.demo.model.AuthenticationResponse;
import com.teleconsulting.demo.model.Patient;
import com.teleconsulting.demo.model.Role;
import com.teleconsulting.demo.repository.PatientRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.teleconsulting.demo.controller.CallHistoryController.decrypt;

@Service
public class PatientServiceImpl implements PatientService{
    private  final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient savePatient(Patient patient) {
        patientRepository.save(patient);
        return patient;
    }


    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Patient getPatientByPhoneNumber(String phoneNumber) {
        return patientRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public Patient findById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @Override
    public Patient updatePatient(Long patientId, Pdetails pdetails) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        patient.setName(pdetails.getName());
        patient.setGender(pdetails.getGender());

        return patientRepository.save(patient);
}

    @Override
    public AuthenticationResponse saveNewPatient(Patient patient){
        Patient patient1 = patientRepository.findByEmail(patient.getEmail()).orElse(null);
        if(patient1 == null) {
            patient1.setPassword(passwordEncoder.encode(patient.getPassword()));
            patient1.setEmail(patient.getEmail());
            patient1.setName(patient.getName());
            patient1.setGender(patient.getGender());
            patient1.setPhoneNumber(patient.getPhoneNumber());
            patient1.setRole(Role.valueOf(Role.USER.toString()));
            patientRepository.save(patient1);
            return new AuthenticationResponse(null, "User Registration was Successful!!");
        }
        else
            return new AuthenticationResponse(null, "Patient Email ID already exist");
    }

    @Override
    public Optional<Patient> getUserByEmail(String email) {
        System.out.println("\nInside impl "+ email);
        return patientRepository.findByEmail(email);
    }

    @Override
    public List<Patient> findAllcallbackPatients() {
        return patientRepository.findByCallbackstatus(true);
    }

    @Override
    public List<Patient> getALLPatient() {
        return patientRepository.findAllPatient();
    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if(patient != null) {
            patient.setDeleteFlag(true);
            patientRepository.save(patient);
        }
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patientList = patientRepository.findAll();
        for(Patient patient : patientList) {
            String temp = patient.getPhoneNumber();
            try{
                patient.setPhoneNumber(decrypt(temp));
            }catch(Exception e)
            {
                System.out.println("\nException in PatientServiceImple findById\n"+e);
            }
        }
        return patientList;
    }

    @Override
    public Patient findByEmail(String email) {
        Patient patient = patientRepository.findByEmail(email).orElse(null);
        if(patient != null)
        {
            String temp = patient.getPhoneNumber();
            try{
                patient.setPhoneNumber(decrypt(temp));
            }catch(Exception e)
            {
                System.out.println("\nException in PatientServiceImple findById\n"+e);
            }
        }
        return patient;
    }
    @Override
    public Long countPatients() {
        return patientRepository.count();
    }
}
