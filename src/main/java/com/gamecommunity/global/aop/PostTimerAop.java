package com.gamecommunity.global.aop;

import com.gamecommunity.domain.post.entity.ApiPostUseTime;
import com.gamecommunity.domain.post.repository.ApiPostUseTimeRepository;
import com.gamecommunity.global.enums.board.BoardName;
import com.gamecommunity.global.enums.game.name.GameName;
import com.gamecommunity.global.enums.game.type.GameType;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j(topic = "TimerAop")
@Aspect
@Component
@RequiredArgsConstructor
public class PostTimerAop {

  private final ApiPostUseTimeRepository apiPostUseTimeRepository;

  @Pointcut("@annotation(postTimer)")
  private void enableTimer(PostTimer postTimer) {
  }

  @Around("enableTimer(postTimer)")
  public Object execute(ProceedingJoinPoint joinPoint, PostTimer postTimer) throws Throwable {
    // 측정 시작 시간
    long startTime = System.currentTimeMillis();

    try {
      // 핵심기능 수행
      Object output = joinPoint.proceed();
      return output;
    } finally {
      // 측정 종료 시간
      long endTime = System.currentTimeMillis();
      // 수행시간 = 종료 시간 - 시작 시간
      long runTime = endTime - startTime;

      // PostTimer 어노테이션에서 받은 값
      Object[] methodArgs = joinPoint.getArgs();
      GameType typeFromParam = (GameType) (GameType) Arrays.stream(methodArgs)
          .filter(arg -> arg instanceof GameType) // GameType인지 확인
          .findFirst().orElse(null); // 만약 찾지 못하면 null을 반환

      GameName gameFromParam = (GameName) Arrays.stream(methodArgs)
          .filter(arg -> arg instanceof GameName)
          .findFirst().orElse(null);

      BoardName boardFromParam = (BoardName) Arrays.stream(methodArgs)
          .filter(arg -> arg instanceof BoardName)
          .findFirst().orElse(null);

      // API 사용시간 및 DB에 기록
      ApiPostUseTime apiPostUseTime = apiPostUseTimeRepository
          .findByGameTypeAndGameNameAndBoardName(typeFromParam, gameFromParam, boardFromParam)
          .orElse(new ApiPostUseTime(typeFromParam, gameFromParam, boardFromParam, 0L));

      apiPostUseTime.addPostUseTime(runTime);

      log.info("[각 게시판 사용시간] 게시판: {} {} {}, Run Time: {} ms, Total Time: {} ms",
          typeFromParam, gameFromParam, boardFromParam, runTime, apiPostUseTime.getTotalTime());

      apiPostUseTimeRepository.save(apiPostUseTime);
    }
  }
}