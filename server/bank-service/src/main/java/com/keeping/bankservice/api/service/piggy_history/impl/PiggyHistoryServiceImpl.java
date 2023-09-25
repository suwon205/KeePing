package com.keeping.bankservice.api.service.piggy_history.impl;

import com.keeping.bankservice.api.controller.piggy_history.response.ShowPiggyHistoryResponse;
import com.keeping.bankservice.api.service.piggy.PiggyService;
import com.keeping.bankservice.api.service.piggy_history.PiggyHistoryService;
import com.keeping.bankservice.api.service.piggy_history.dto.AddPiggyHistoryDto;
import com.keeping.bankservice.domain.piggy.Piggy;
import com.keeping.bankservice.domain.piggy_history.PiggyHistory;
import com.keeping.bankservice.domain.piggy_history.repository.PiggyHistoryQueryRepository;
import com.keeping.bankservice.domain.piggy_history.repository.PiggyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PiggyHistoryServiceImpl implements PiggyHistoryService {

    private PiggyService piggyService;
    private PiggyHistoryRepository piggyHistoryRepository;
    private PiggyHistoryQueryRepository piggyHistoryQueryRepository;

    @Override
    public Long addPiggyHistory(String memberKey, AddPiggyHistoryDto dto) {
        String lastSavingHistoryName = piggyHistoryQueryRepository.lastSavingHistoryName(dto.getPiggy());

        // TODO: 이미 등록된 저금 내역이 없을 때 이름 지정하는 코드 추가해야 함!

        String name = Integer.toString(Integer.parseInt(lastSavingHistoryName) + 1);

        PiggyHistory piggyHistory = PiggyHistory.toPiggyHistory(dto.getPiggy(), name, dto.getMoney(), dto.getBalance());
        PiggyHistory savePiggyHistory = piggyHistoryRepository.save(piggyHistory);

        return savePiggyHistory.getId();
    }

    @Override
    public List<ShowPiggyHistoryResponse> showPiggyHistory(String memberKey, Long piggyId) {
        Piggy piggy = piggyService.isValidPiggy(memberKey, piggyId);

        List<ShowPiggyHistoryResponse> showPiggyHistory = piggyHistoryQueryRepository.showPiggyHistory(piggyId);

        return showPiggyHistory;
    }
}
