package net.wohlfart.basic.time;

/**
 * This package serves to provide delta times for machine and
 * performance independent animation and input controls.
 *
 * Just create a timer implementation with a clock and call
 * the getDelta() to get the time in seconds since the last
 * call of getDelta().
 *
 *
 * for lwjgl this looks like:
 *
 *  timer = new TimerImpl(LwjglClockImpl());
 *  interval = timer.getDelta();
 *
 *
 */

