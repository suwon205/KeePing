package com.keeping.bankservice.api.service.piggy.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.keeping.bankservice.api.controller.piggy.response.ShowPiggyResponse;
import com.keeping.bankservice.api.service.account.AccountService;
import com.keeping.bankservice.api.service.account.dto.SavingPiggyDto;
import com.keeping.bankservice.api.service.account.dto.WithdrawMoneyDto;
import com.keeping.bankservice.api.service.piggy.PiggyService;
import com.keeping.bankservice.api.service.piggy.dto.AddPiggyDto;
import com.keeping.bankservice.api.service.piggy.dto.ShowPiggyDto;
import com.keeping.bankservice.api.service.piggy_history.dto.AddPiggyHistoryDto;
import com.keeping.bankservice.api.service.piggy_history.PiggyHistoryService;
import com.keeping.bankservice.domain.piggy.Piggy;
import com.keeping.bankservice.domain.piggy.repository.PiggyQueryRepository;
import com.keeping.bankservice.domain.piggy.repository.PiggyRepository;
import com.keeping.bankservice.global.exception.NoAuthorizationException;
import com.keeping.bankservice.global.exception.NotFoundException;
import com.keeping.bankservice.global.exception.ServerException;
import com.keeping.bankservice.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PiggyServiceImpl implements PiggyService {

    @Value("${file.path.piggy}")
    private String piggyPath;

    private final PiggyRepository piggyRepository;
    private final PiggyQueryRepository piggyQueryRepository;
    private final AccountService accountService;
    private final PiggyHistoryService piggyHistoryService;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtils redisUtils;

    @Override
    public Long addPiggy(String memberKey, AddPiggyDto dto) throws IOException {
        String piggyAccountNumber = createNewPiggyAccountNumber();

        if(dto.getUploadImage() == null) {
            throw new NotFoundException("404", HttpStatus.NOT_FOUND, "이미지가 존재하지 않습니다.");
        }

        File folder = new File(piggyPath);
        if(!folder.exists()) {
            folder.mkdirs();
        }

        MultipartFile file = dto.getUploadImage();
        String originalFileName = file.getOriginalFilename();

        if(!originalFileName.isEmpty()) {
            String saveFileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));

            file.transferTo(new File(folder, saveFileName));

            Piggy piggy = Piggy.toPiggy(memberKey, piggyAccountNumber, dto.getContent(), dto.getGoalMoney(), passwordEncoder.encode(dto.getAuthPassword()), originalFileName, saveFileName);
            Piggy savePiggy = piggyRepository.save(piggy);

            return savePiggy.getId();
        }

        throw new ServerException("503", HttpStatus.SERVICE_UNAVAILABLE, "저금통 개설 과정 중 문제가 생겼습니다. 잠시 후 다시 시도해 주세요.");
    }

    @Override
    public List<ShowPiggyResponse> showPiggy(String memberKey) throws IOException {
        List<ShowPiggyDto> result = piggyQueryRepository.showPiggy(memberKey);

        List<ShowPiggyResponse> response = new ArrayList<>();

        for(ShowPiggyDto dto: result) {
            File file = new File(piggyPath + "\\" + dto.getSavedImage());
            byte[] byteImage = new byte[(int)file.length()];
            FileInputStream fis = new FileInputStream(file);
            fis.read(byteImage);
            String base64Image = new String(Base64.encodeBase64(byteImage));

            ShowPiggyResponse piggyResponse = ShowPiggyResponse.toResponse(dto, base64Image);
            response.add(piggyResponse);
        }

        return response;
    }

    @Override
    public void savingPiggy(String memberKey, SavingPiggyDto dto) {
        Piggy piggy = piggyRepository.findByAccountNumber(dto.getPiggyAccountNumber())
                .orElseThrow(() -> new NotFoundException("404", HttpStatus.NOT_FOUND, "해당하는 계좌가 존재하지 않습니다."));

        if(!piggy.getAccountNumber().equals(dto.getPiggyAccountNumber())) {
            throw new NoAuthorizationException("401", HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다.");
        }

        WithdrawMoneyDto withdrawMoneyDto = WithdrawMoneyDto.toDto(dto.getAccountNumber(), dto.getMoney());
        accountService.withdrawMoney(memberKey, withdrawMoneyDto);

        // TODO: 출금 거래내역을 등록하는 코드 필요!!

        int balance = piggy.getBalance() + dto.getMoney();
        piggy.updateBalance(dto.getMoney());

        AddPiggyHistoryDto addPiggyHistoryDto = AddPiggyHistoryDto.toDto(piggy, dto.getMoney(), balance);
        piggyHistoryService.addPiggyHistory(memberKey, addPiggyHistoryDto);
    }

    private String createNewPiggyAccountNumber() throws JsonProcessingException {
        Random rand = new Random();

        int num = 0;
        do {
            num = rand.nextInt(888889) + 111111;
        }
        while(redisUtils.getRedisValue("Piggy_" + String.valueOf(num), String.class) != null);

        String randomNumber = String.valueOf(num);
        redisUtils.setRedisValue("Piggy_" + randomNumber, "1");

        String validCode = "";

        int divideNum = num;
        for(int i = 0; i < 3; i++) {
            int num1 = divideNum % 10;
            divideNum /= 10;
            int num2 = divideNum % 10;

            int sum = 0;
            if(i == 1) {
                sum = (num1 * num2) % 10;
            }
            else {
                sum = (num1 + num2) % 10;
            }
            divideNum /= 10;

            validCode = String.valueOf(sum) + validCode;
        }

        return "172-" + randomNumber + "-" + validCode + "-27"; // 3-6-3-2
    }
}