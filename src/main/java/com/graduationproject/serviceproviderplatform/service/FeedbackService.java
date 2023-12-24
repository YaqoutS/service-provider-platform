package com.graduationproject.serviceproviderplatform.service;

import com.graduationproject.serviceproviderplatform.model.ServiceFeedback;
import com.graduationproject.serviceproviderplatform.repository.ServiceFeedbackRepository;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Service
public class FeedbackService {
    private ServiceFeedbackRepository serviceFeedbackRepository;

    public FeedbackService(ServiceFeedbackRepository serviceFeedbackRepository) {
        this.serviceFeedbackRepository = serviceFeedbackRepository;
    }

    @Transactional
    public void delete(ServiceFeedback feedback) {
        serviceFeedbackRepository.delete(feedback);
    }
}
