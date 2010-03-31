package com.jetbrains.python.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.jetbrains.python.psi.stubs.PyParameterListStub;
import org.jetbrains.annotations.NotNull;

/**
 * Represents function parameter list.
 * Date: 29.05.2005
 */
public interface PyParameterList extends PyElement, StubBasedPsiElement<PyParameterListStub>, NameDefiner {

  /**
   * Extracts the individual parameters.
   * Note that tuple parameters are flattened by this method.
   * @return a possibly empty array of named paramaters.
   */
  PyParameter[] getParameters();

  /**
   * Adds a paramter to list, after all other parameters.
   * @param param what to add
   */
  void addParameter(PyNamedParameter param);


  /**
   * @return true iff this list contains an '*args'-type parameter.
   */
  boolean hasPositionalContainer();
  
  /**
   * @return true iff this list contains a '**kwargs'-type parameter.
   */
  boolean hasKeywordContainer();

  /**
   * Checks is this parameter list is the same or is a superset of another parameter list.
   * (The reverse is only true is the lists are the same.)
   * @param another what to compare to
   * @return true if this list is a superset of another.
   */
  boolean isCompatibleTo(@NotNull PyParameterList another);

}
