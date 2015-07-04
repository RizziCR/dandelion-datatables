/*
 * [The "BSD licence"]
 * Copyright (c) 2012 Dandelion
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
package com.github.dandelion.datatables.core.option;

import java.util.List;

import com.github.dandelion.core.asset.generator.js.JsFunction;

/**
 * <p>
 * Callback called by DataTables.
 * <p>
 * A callback is composed of:
 * <ul>
 * <li>a {@link CallbackType} available among all callbacks proposed by
 * DataTables</li>
 * <li>a {@link JsFunction} that is executed in the callback</li>
 * </ul>
 * 
 * @see CallbackType
 * 
 * @author Thibault Duchateau
 * @since 0.8.9
 */
public class Callback {

   private CallbackType type;
   private JsFunction function;

   public Callback(CallbackType type, String functionContent) {
      this.type = type;
      this.function = new JsFunction(functionContent, this.type.getArgs());
   }

   public Callback(CallbackType type, JsFunction function) {
      this.type = type;
      this.function = function;
   }

   public CallbackType getType() {
      return type;
   }

   public void setType(CallbackType type) {
      this.type = type;
   }

   public JsFunction getFunction() {
      return function;
   }

   public void setFunction(JsFunction function) {
      this.function = function;
   }

   public void appendCode(String code) {
      this.function.appendCode(code);
   }

   public static boolean hasCallback(CallbackType callbackType, List<Callback> callbacks) {
      if (callbacks != null) {
         for (Callback callback : callbacks) {
            if (callback.getType().equals(callbackType)) {
               return true;
            }
         }
      }
      return false;
   }

   public static Callback findByType(CallbackType type, List<Callback> callbacks) {
      for (Callback callback : callbacks) {
         if (callback.getType().equals(type)) {
            return callback;
         }
      }
      return null;
   }

   @Override
   public String toString() {
      return "Callback [type=" + type + ", function=" + function + "]";
   }
}