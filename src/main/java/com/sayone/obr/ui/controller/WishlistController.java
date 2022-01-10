package com.sayone.obr.ui.controller;

import com.sayone.obr.dto.UserDto;
import com.sayone.obr.entity.BookEntity;
import com.sayone.obr.entity.WishlistEntity;
import com.sayone.obr.exception.UserServiceException;
import com.sayone.obr.exception.WishlistErrors;
import com.sayone.obr.repository.BookRepository;
import com.sayone.obr.repository.WishlistRepository;
import com.sayone.obr.service.UserService;
import com.sayone.obr.service.WishlistService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;



@RestController
@RequestMapping("wishlist")
public class WishlistController {
    @Autowired
    WishlistService wishlistService;
    @Autowired
    UserService userService;
    @Autowired
    WishlistRepository wishlistRepository;
    @Autowired
    BookRepository bookRepository;

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${wishlistController.authorizationHeader.description}", paramType = "header")})
    @PostMapping("add/{bid}")
    public ResponseEntity<WishlistEntity> addToWishlist(@PathVariable(value = "bid") Long bookId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());
        Optional <BookEntity> bookEntity=bookRepository.findByBookId(bookId);
        if(bookEntity.isEmpty())
            throw new UserServiceException(WishlistErrors.NO_BOOK_FOUND.getErrorMessage());
        WishlistEntity wishlistEntity = wishlistService.addBookToWishlist(user.getId(), bookId);
        return new ResponseEntity<>(wishlistEntity, HttpStatus.OK);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${wishlistController.authorizationHeader.description}", paramType = "header")})
    @DeleteMapping("delete/{bid}")
    public String deleteBookFromWishlist(@PathVariable (value = "bid") Long bookId){
        if(bookId == null){
       throw new UserServiceException(WishlistErrors.NO_RECORD_FOUND.getErrorMessage());
        }
        else {
            Authentication auth=SecurityContextHolder.getContext().getAuthentication();
            UserDto user = userService.getUser(auth.getName());
            WishlistEntity wishlistEntity2= wishlistRepository.findUserById(user.getId());
            if(wishlistEntity2==null) {
                throw new UserServiceException(WishlistErrors.NO_USER_FOUND.getErrorMessage());
            }
            wishlistService.removeBookFromWishlist(wishlistEntity2.getWishlistId(),bookId);
//
        }
        return "Book deleted successfully from wishlist";
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${wishlistController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("get")
    public List<BookEntity> getWishlistItems(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserDto user = userService.getUser(auth.getName());
        return wishlistService.getWishlistItems(user.getId());
    }


    @ApiImplicitParams({@ApiImplicitParam(name = "authorization",
            value = "${wishlistController.authorizationHeader.description}", paramType = "header")})
    @GetMapping("get/{category}")
    public List<Map<String,Object>>  getWishlistItemsByCat(@PathVariable (value = "category") String category){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto user = userService.getUser(auth.getName());

        if(wishlistService.getWishlistItemsByCat(user.getId(),category).isEmpty())
            throw new UserServiceException(WishlistErrors.NO_RECORD_FOUND.getErrorMessage());
        return wishlistService.getWishlistItemsByCat(user.getId(),category);
    }
}

