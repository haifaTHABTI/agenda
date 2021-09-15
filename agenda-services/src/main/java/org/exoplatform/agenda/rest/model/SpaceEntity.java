/*
 * Copyright (C) 2003-2015 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
*/

package org.exoplatform.agenda.rest.model;

import java.util.List;

public class SpaceEntity extends BaseEntity {
  private static final long serialVersionUID = -5407676622915680099L;

  public SpaceEntity() {
  }

  public SpaceEntity(String id) {
    super(id);
  }

  public SpaceEntity setDisplayName(String displayName) {
    setProperty("displayName", displayName);
    return this;
  }

  public String getDisplayName() {
    return getString("displayName");
  }

  public SpaceEntity setTemplate(String displayName) {
    setProperty("template", displayName);
    return this;
  }

  public String getTemplate() {
    return getString("template");
  }

  public SpaceEntity setUrl(String url) {
    setProperty("url", url);
    return this;
  }

  public String getUrl() {
    return getString("url");
  }

  public SpaceEntity setGroupId(String groupId) {
    setProperty("groupId", groupId);
    return this;
  }

  public String getGroupId() {
    return getString("groupId");
  }

  public SpaceEntity setPrettyName(String prettyName) {
    setProperty("prettyName", prettyName);
    return this;
  }

  public String getPrettyName() {
    return getString("prettyName");
  }

  public SpaceEntity setAvatarUrl(String avatarUrl) {
    setProperty("avatarUrl", avatarUrl);
    return this;
  }

  public String getAvatarUrl() {
    return getString("avatarUrl");
  }

  public SpaceEntity setBannerUrl(String bannerUrl) {
    setProperty("bannerUrl", bannerUrl);
    return this;
  }

  public String getBannerUrl() {
    return getString("bannerUrl");
  }

  public SpaceEntity setApplications(List<DataEntity> applications) {
    setProperty("applications", applications);
    return this;
  }

  public SpaceEntity setVisibility(String visibility) {
    setProperty("visibility", visibility);
    return this;
  }

  public String getVisibility() {
    return getString("visibility");
  }

  public SpaceEntity setSubscription(String subscription) {
    setProperty("subscription", subscription);
    return this;
  }

  public String getSubscription() {
    return getString("subscription");
  }

  public SpaceEntity setDescription(String description) {
    setProperty("description", description);
    return this;
  }

  public String getDescription() {
    return getString("description");
  }

  public SpaceEntity setIsManager(boolean isManager) {
    setProperty("isManager", isManager);
    return this;
  }

  public Boolean getIsManager() {
    return (Boolean) getProperty("isManager");
  }

  public SpaceEntity setIsRedactor(boolean isRedactor) {
    setProperty("isRedactor", isRedactor);
    return this;
  }

  public Boolean getIsRedactor() {
    return (Boolean) getProperty("isRedactor");
  }

  public SpaceEntity setCreatedTime(String creationTime) {
    setProperty("createdTime", creationTime);
    return this;
  }

  public String getCreatedTime() {
    return (String) getProperty("createdTime");
  }

  public SpaceEntity setCanEdit(boolean canEdit) {
    setProperty("canEdit", canEdit);
    return this;
  }

  public Boolean getCanEdit() {
    return (Boolean) getProperty("canEdit");
  }

  public SpaceEntity setIsMember(boolean isMember) {
    setProperty("isMember", isMember);
    return this;
  }

  public Boolean getIsMember() {
    return (Boolean) getProperty("isMember");
  }

  public SpaceEntity setMembersCount(int membersCount) {
    setProperty("membersCount", membersCount);
    return this;
  }

  public Integer getMembersCount() {
    return (Integer) getProperty("membersCount");
  }

}
