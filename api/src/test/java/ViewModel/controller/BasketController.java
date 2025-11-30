package ViewModel.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;

import com.codewithd.todolist.model.user;
import com.codewithd.todolist.viewModel.controller.basketController;
import com.codewithd.todolist.viewModel.service.authencationService;
import com.codewithd.todolist.viewModel.service.basketService;

public class BasketController {
    private basketController basketController;
    private basketService mockBasketService;
    private authencationService mockAuthService;
    private static final String VALID_TOKEN = "valid_token";
    private static final Integer userId = 1;

    @BeforeEach
    public void setupBasketController(){
        mockBasketService = mock(basketService.class);
        mockAuthService = mock(authencationService.class);
        basketController = new basketController(mockBasketService, mockAuthService);

    }
    
}
