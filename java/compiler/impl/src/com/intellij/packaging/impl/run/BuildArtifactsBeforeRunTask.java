/*
 * Copyright 2000-2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.packaging.impl.run;

import com.intellij.execution.BeforeRunTask;
import com.intellij.openapi.project.Project;
import com.intellij.packaging.artifacts.Artifact;
import com.intellij.packaging.artifacts.ArtifactPointer;
import com.intellij.packaging.artifacts.ArtifactPointerManager;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author nik
 */
public class BuildArtifactsBeforeRunTask extends BeforeRunTask<BuildArtifactsBeforeRunTask> {
  @NonNls public static final String NAME_ATTRIBUTE = "name";
  @NonNls public static final String ARTIFACT_ELEMENT = "artifact";
  private List<ArtifactPointer> myArtifactPointers = new ArrayList<ArtifactPointer>();
  private final Project myProject;

  public BuildArtifactsBeforeRunTask(Project project) {
    super(BuildArtifactsBeforeRunTaskProvider.ID);
    myProject = project;
  }

  @Override
  public void readExternal(Element element) {
    super.readExternal(element);
    final List<Element> children = element.getChildren(ARTIFACT_ELEMENT);
    final ArtifactPointerManager pointerManager = ArtifactPointerManager.getInstance(myProject);
    for (Element child : children) {
      myArtifactPointers.add(pointerManager.createPointer(child.getAttributeValue(NAME_ATTRIBUTE)));
    }
  }

  @Override
  public void writeExternal(Element element) {
    super.writeExternal(element);
    for (ArtifactPointer pointer : myArtifactPointers) {
      element.addContent(new Element(ARTIFACT_ELEMENT).setAttribute(NAME_ATTRIBUTE, pointer.getArtifactName()));
    }
  }

  @Override
  public BeforeRunTask clone() {
    final BuildArtifactsBeforeRunTask task = (BuildArtifactsBeforeRunTask)super.clone();
    task.myArtifactPointers = new ArrayList<ArtifactPointer>(myArtifactPointers);
    return task;
  }

  @Override
  public int getItemsCount() {
    return myArtifactPointers.size();
  }

  public List<ArtifactPointer> getArtifactPointers() {
    return Collections.unmodifiableList(myArtifactPointers);
  }

  public void setArtifactPointers(List<ArtifactPointer> artifactPointers) {
    myArtifactPointers = new ArrayList<ArtifactPointer>(artifactPointers);
  }

  public void addArtifact(Artifact artifact) {
    final ArtifactPointer pointer = ArtifactPointerManager.getInstance(myProject).createPointer(artifact);
    if (!myArtifactPointers.contains(pointer)) {
      myArtifactPointers.add(pointer);
    }
  }

  public void removeArtifact(@NotNull Artifact artifact) {
    removeArtifact(ArtifactPointerManager.getInstance(myProject).createPointer(artifact));
  }

  public void removeArtifact(final @NotNull ArtifactPointer pointer) {
    myArtifactPointers.remove(pointer);
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    BuildArtifactsBeforeRunTask that = (BuildArtifactsBeforeRunTask)o;

    if (!myArtifactPointers.equals(that.myArtifactPointers)) return false;
    if (!myProject.equals(that.myProject)) return false;

    return true;
  }

  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + myArtifactPointers.hashCode();
    result = 31 * result + myProject.hashCode();
    return result;
  }
}
