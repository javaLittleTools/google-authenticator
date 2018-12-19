/*
 * Copyright 2010 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sxl.auth.util;


/**
 * A class for handling a variety of utility things.  This was mostly made
 * because I needed to centralize dialog related constants. I foresee this class
 * being used for other code sharing across Activities in the future, however.
 *
 * @author alexei@czeskis.com (Alexei Czeskis)
 *
 */
public class Utilities {
  // Dialog IDs
  public static final int DOWNLOAD_DIALOG = 0;
  public static final int MULTIPLE_ACCOUNTS_DIALOG = 1;
  static final int INVALID_QR_CODE = 3;
  static final int INVALID_SECRET_IN_QR_CODE = 7;

  public static final long SECOND_IN_MILLIS = 1000;
  public static final long MINUTE_IN_MILLIS = 60 * SECOND_IN_MILLIS;

  // Constructor -- Does nothing yet
  private Utilities() { }

  public static final long millisToSeconds(long timeMillis) {
    return timeMillis / 1000;
  }

  public static final long secondsToMillis(long timeSeconds) {
    return timeSeconds * 1000;
  }
}
