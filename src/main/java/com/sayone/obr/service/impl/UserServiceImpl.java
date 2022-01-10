package com.sayone.obr.service.impl;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.UserEntity;
import com.sayone.obr.exception.*;
import com.sayone.obr.repository.UserRepository;
import com.sayone.obr.security.SecurityConstants;
import com.sayone.obr.service.EmailService;
import com.sayone.obr.service.UserService;
import com.sayone.obr.shared.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    Utils utils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    EmailService emailService;


//user
    @Override
    public UserDto createUser(UserDto user) {

        if (userRepository.findByEmail(user.getEmail()) !=null)
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity );

//        userEntity.setUserId("testUser");
//        userEntity.setEncryptedPassword("test");

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));


        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);
        return returnValue;

    }

    @Override
    public UserDto getUser(String email) {

        UserEntity userEntity= userRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto getUserByUserId(String userId)
    {
        UserDto returnValue = new UserDto();
        UserEntity userEntity= userRepository.getUserByUserId(userId);
        if(userEntity== null) throw new UsernameNotFoundException(userId);
        BeanUtils.copyProperties(userEntity,returnValue);
        return returnValue;

    }



    @Override
    public UserDto updateUser( String userId, UserDto user) {
        UserDto returnValue = new UserDto();
        Optional<UserEntity> userEntity = userRepository.findByUserId(userId);
        if(userEntity.isEmpty()) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.get().setFirstName(user.getFirstName());
        userEntity.get().setLastName(user.getLastName());
        userEntity.get().setUserStatus(user.getUserStatus());
        UserEntity updatedUserDetails = userRepository.save(userEntity.get());
        BeanUtils.copyProperties(updatedUserDetails, returnValue);
        return returnValue;
    }

    @Override
    public void deleteUserById(String userId){
        userRepository.deleteByUserId(userId);
    }

    //publisher,admin

    @Override
    public UserDto getPublisherById(String userId) {

        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findByPublisherId(userId,"publisher");

        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_PUBLISHER_FOUND.getPublisherErrorMessages());

        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updatePublisher(String userId, UserDto userDto) {

        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByPublisherId(userId,"publisher");

        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_PUBLISHER_FOUND.getPublisherErrorMessages());

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setPhoneNumber(userDto.getPhoneNumber());
        userEntity.setAddress(userDto.getAddress());

        UserEntity updatedPublisherDetails = userRepository.save(userEntity);
        BeanUtils.copyProperties(updatedPublisherDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deletePublisher(String userId) {
        UserEntity userEntity = userRepository.findByPublisherId(userId,"publisher");

        if (userEntity == null) {
            throw new IllegalStateException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());
        }
        userRepository.delete(userEntity);
    }

    @Override
    public UserDto getAllPublishersByRole() {

        UserEntity userEntity = userRepository.findAllByRole("publisher");
        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getAllUsersByRole() {

        UserEntity userEntity = userRepository.findAllByRole("user");
        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepository.findByPublisherId(userId,"user");

        if (userEntity == null) throw new IllegalStateException(PublisherErrorMessages.NO_RECORD_FOUND.getPublisherErrorMessages());

        userRepository.delete(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserEntity updateUserEmail( String emailId, UserDto user ) throws Exception {
        System.out.println(emailId);
        UserDto returnValue = new UserDto();
        Optional<UserEntity> userEntity = userRepository.findByUserId(user.getUserId());
        if(userEntity.isEmpty()) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
//        BeanUtils.copyProperties(updatedUserDetails, returnValue);
//        return returnValue;

        String token = utils.generateEmailVerificationToken(userEntity.get().getUserId());
        UserEntity updatedUserDetails = userRepository.findByEmail(user.getEmail());

//        System.out.println("token");
//        System.out.println(token);
//        UserEntity updatedToken = new UserEntity();
//        updatedToken.setEmailVerificationToken(token);
//        userRepository.save(updatedToken);

//        userEntity.(token);

//        UserEntity updatedUserDetails = userRepository.findByEmail(emailId);

//        System.out.println("updated email successfully");

        String link = SecurityConstants.USER_CREATE_EMAIL_LINK + token;
        EmailBuilder emailBuilder = new EmailBuilder();
        Boolean status=emailService.send(emailId,
                emailBuilder.buildUserActivateContent(userEntity.get().getFirstName(), link));
        if(!status) throw new UserServiceException(WishlistErrors.EMAIL_FAIL.getErrorMessage());
            System.out.println("email send ................");
//        userRepository.save(userEntity.get());s
        Boolean verifier = verifyEmailToken(token);
        if(!verifier) throw new UserServiceException(WishlistErrors.TOKEN_NOT_FOUND.getErrorMessage());
//        UserEntity updatedEmail = new UserEntity();
        updatedUserDetails.setEmail(emailId);
        return userRepository.save(updatedUserDetails);
    }

    private Boolean verifyEmailToken(String token) {

//        UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
//        if (userEntity == null)
//            throw new UserServiceException(WishlistErrors.TOKEN_NOT_FOUND.getErrorMessage());

        if(Utils.hasTokenExpired(token)) throw new UserServiceException(WishlistErrors.EMAIL_TOKEN_EXPIRED.getErrorMessage());
//        userEntity.setEmailVerificationToken(null);
//        userEntity.setEmailVerification(Boolean.TRUE);
        return true;
    }


}
