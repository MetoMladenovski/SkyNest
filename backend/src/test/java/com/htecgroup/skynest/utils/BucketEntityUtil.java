package com.htecgroup.skynest.utils;

import com.htecgroup.skynest.model.entity.BucketEntity;
import com.htecgroup.skynest.model.entity.CompanyEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BucketEntityUtil {
  protected static final CompanyEntity companyEntity = new CompanyEntity();
  protected static String description = "Description";
  protected static Long size = 1000L;
  protected static boolean privateBucket = false;

  public static BucketEntity getPrivateBucket() {
    BucketEntity bucketEntity = new BucketEntity(companyEntity, description, size, privateBucket);
    bucketEntity.setName("Name");
    bucketEntity.setCreatedBy(UserEntityUtil.getVerified());
    return bucketEntity;
  }
}
