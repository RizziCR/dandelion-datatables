/*
 * [The "BSD licence"]
 * Copyright (c) 2013-2015 Dandelion
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * 3. Neither the name of Dandelion nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.dandelion.datatables.thymeleaf.util;

import org.thymeleaf.dom.Element;

/**
 * <p>
 * Utilities used to manipulate the DOM.
 * 
 * @author Thibault Duchateau
 */
public class DomUtils {

   /**
    * <p>
    * Recursive search for an element within the given node in the DOM tree.
    * <p>
    * Many thanks to Emanuel Rabina :-)
    * 
    * @param element
    *           Node to initiate the search from.
    * @param elementName
    *           Name of the element to look for.
    * @return Element with the given name, or <tt>null</tt> if the element could
    *         not be found.
    */
   public static Element findElement(Element element, String elementName) {

      if (element.getOriginalName().equals(elementName)) {
         return element;
      }
      for (Element child : element.getElementChildren()) {
         Element result = findElement(child, elementName);
         if (result != null) {
            return result;
         }
      }
      return null;
   }

   /**
    * <p>
    * Recursive search for an element, with a particular attribute/value pair,
    * within the given node in the DOM tree.
    * 
    * @param element
    *           Node to initiate the search from.
    * @param elementName
    *           Name of the element to look for.
    * @param attributeName
    *           Attribute that the element must have.
    * @param attributeValue
    *           Value of the attribute that the element must have.
    * @return Element with the given name, or <tt>null</tt> if the element could
    *         not be found.
    */
   public static Element findElement(Element element, String elementName, String attributeName, String attributeValue) {

      if (element.getOriginalName().equals(elementName) && element.hasAttribute(attributeName)
            && element.getAttributeValue(attributeName).equals(attributeValue)) {
         return element;
      }
      for (Element child : element.getElementChildren()) {
         Element result = findElement(child, elementName, attributeName, attributeValue);
         if (result != null) {
            return result;
         }
      }
      return null;
   }
}