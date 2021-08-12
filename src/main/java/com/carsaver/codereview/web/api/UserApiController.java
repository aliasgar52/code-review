package com.carsaver.codereview.web.api;

import com.carsaver.codereview.model.User;
import com.carsaver.codereview.service.EmailService;
import com.carsaver.codereview.service.UserService;
import com.carsaver.codereview.service.ZipCodeLookupService;
import liquibase.pro.packaged.U;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserApiController {



    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ZipCodeLookupService zipCodeLookupService;

    @GetMapping("/users/create")
    public User createUser(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email) {
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);

        if(!email.contains("@test.com")) {
            newUser.enabled = true;
        }

        User user = userService.save(newUser);

        if(user.isEnabled()) {
            emailService.sendConfirmation(email);
        }

        return user;
    }

    /**
     * updates user's address
     * @param id - assume valid existing id
     * @param zipCode - assume valid zipCode
     * @param city - assume valid if present otherwise null
     * @return updated User
     */
    @GetMapping("/users/updateLocation")
    public User updateUserLocation(@RequestParam Long id, @RequestParam String zipCode, @RequestParam(required = false) String city) {
        User user = userService.findById(id).orElse(new User());

        user.setZipCode(zipCode);
        user.setCity(Optional.ofNullable(city).orElse(zipCodeLookupService.lookupCityByZip(zipCode)));

        return userService.save(user);
    }

    @GetMapping("/users/delete")
    public void deleteUser(@RequestParam String userId) {
        userService.deleteById(Long.parseLong(userId));
    }
}
