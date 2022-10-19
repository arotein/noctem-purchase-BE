package noctem.purchaseService.purchase.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noctem.purchaseService.purchase.domain.repository.StatisticsRepository;
import noctem.purchaseService.purchase.dto.response.PopularMenuResDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;

    @Override
    public List<PopularMenuResDto> getPopularMenuTop3ByStore(Long storeId) {
        List<PopularMenuResDto> top3SizeIdList = statisticsRepository.findPopularMenuTop3ByStore(storeId);
        top3SizeIdList.forEach(e -> e.setRank(top3SizeIdList.indexOf(e) + 1));
        return top3SizeIdList;
    }

    @Override
    public List<PopularMenuResDto> getPopularMenuTop5() {
        List<PopularMenuResDto> top5SizeIdList = statisticsRepository.findPopularMenuTop5();
        top5SizeIdList.forEach(e -> e.setRank(top5SizeIdList.indexOf(e) + 1));
        return top5SizeIdList;
    }
}
