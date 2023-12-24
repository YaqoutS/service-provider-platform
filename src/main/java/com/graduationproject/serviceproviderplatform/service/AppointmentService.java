package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.Appointment;
import com.graduationproject.serviceproviderplatform.repository.AppointmentRepository;

public class AppointmentService {
    private AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void delete(Appointment appointment) {
        appointmentRepository.delete(appointment);
    }
}
