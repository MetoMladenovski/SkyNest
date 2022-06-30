package com.htecgroup.skynest.service;

import com.htecgroup.skynest.model.dto.BucketDto;
import com.htecgroup.skynest.model.request.BucketCreateRequest;
import com.htecgroup.skynest.model.response.BucketResponse;

import java.util.List;
import java.util.UUID;

public interface BucketService {

  BucketResponse createBucket(BucketCreateRequest bucketCreateRequest);

  BucketResponse getBucket(UUID uuid);

  List<BucketResponse> listAllBuckets();

  BucketDto findBucketById(UUID uuid);

  void deleteBucket(UUID uuid);

  BucketDto findBucketByName(String name);
}
