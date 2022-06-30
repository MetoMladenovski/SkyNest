package com.htecgroup.skynest.service.impl;

import com.htecgroup.skynest.exception.UserNotFoundException;
import com.htecgroup.skynest.exception.action.ActionTypeNotFound;
import com.htecgroup.skynest.model.entity.ActionEntity;
import com.htecgroup.skynest.model.entity.ActionType;
import com.htecgroup.skynest.model.entity.ObjectEntity;
import com.htecgroup.skynest.repository.ActionRepository;
import com.htecgroup.skynest.repository.ActionTypeRepository;
import com.htecgroup.skynest.repository.UserRepository;
import com.htecgroup.skynest.service.ActionService;
import com.htecgroup.skynest.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ActionServiceImpl implements ActionService {

  private final ActionRepository actionRepository;
  private final ActionTypeRepository actionTypeRepository;
  private final UserRepository userRepository;
  private final CurrentUserService currentUserService;

  @Override
  public ActionEntity recordAction(Set<ObjectEntity> objects, ActionType actionType) {

    ActionEntity actionEntity = new ActionEntity();

    actionEntity.setActionType(
        actionTypeRepository.findByName(actionType.text).orElseThrow(ActionTypeNotFound::new));

    actionEntity.setUser(
        userRepository
            .findById(currentUserService.getLoggedUser().getUuid())
            .orElseThrow(UserNotFoundException::new));

    actionEntity.setObjects(objects);

    ActionEntity savedEntity = actionRepository.save(actionEntity);

    log.info(
        "User {} has performed action {} on objects {}",
        savedEntity.getUser().getEmail(),
        savedEntity.getActionType().getName(),
        savedEntity.getObjects().stream().map(ObjectEntity::getId).collect(Collectors.toList()));

    return savedEntity;
  }

  @Override
  public List<ActionEntity> getActionsForUser(UUID userId) {
    return actionRepository.findAllByUserIdOrderByPerformedOnDesc(userId);
  }

  @Override
  public List<ActionEntity> getActionsForCurrentUser() {
    return getActionsForUser(currentUserService.getLoggedUser().getUuid());
  }

  @Override
  public List<ActionEntity> getActionsForObject(UUID objectId) {
    return actionRepository.findAllByObjectIdOrderByPerformedOnDesc(objectId);
  }
}