package com.htecgroup.skynest.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "folder")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FolderEntity extends ObjectEntity {

  private static final long serialVersionUID = 6107706031575841420L;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_folder_id")
  private FolderEntity parentFolder;

  @ManyToOne
  @JoinColumn(name = "bucket_id", nullable = false)
  private BucketEntity bucket;
}