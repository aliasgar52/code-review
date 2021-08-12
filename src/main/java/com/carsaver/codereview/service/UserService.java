package com.carsaver.codereview.service;

import com.carsaver.codereview.model.User;
import com.carsaver.codereview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toMap;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EmailService emailService;



    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public List<User> findAll(){
        return repository.findAllByOrderByIdAsc();
    }


    public User save(User user){
        return repository.save(user);
    }

    public void deleteById(Long userId){
        repository.deleteById(userId);
    }


    public User updateEmail(User user, String email) {
        try {
            if (user != null) {
                if (user.getId() != null)
                    if (email != null) {
                        user.setEmail(email);
                        repository.save(user);
                        emailService.sendConfirmation(email);
                    }
                else
                    return user;
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    public Map<Long, String> getNames() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(toMap(User::getId, user ->
                   user.getFirstName() + ", " + user.getFirstName()));
    }

}
