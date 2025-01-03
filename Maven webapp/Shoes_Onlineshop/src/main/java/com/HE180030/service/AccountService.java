package com.HE180030.service;

import com.HE180030.dto.AccountDTO;

import java.util.List;

public interface AccountService {
    AccountDTO login(String username, String password);

    void insertAccountDTO(String user, String pass, boolean isSell, boolean isAdmin, String email);

    void deleteAccountDTOByID(int id);

    void editAccountDTO(String username, String password, boolean isSell, boolean isAdmin, String email, int uID);

    void updateProfile(String username, String password, String email, int uID);

    void signIn(String user, String pass);

    int getIdByUsername(String username);

    AccountDTO getByName(String username);

    List<AccountDTO> getAllAccountDTOs();

    AccountDTO getAccountDTOById(int id);

    List<AccountDTO> getListAccountDTOsByPage(List<AccountDTO> list, int start, int end);
}
