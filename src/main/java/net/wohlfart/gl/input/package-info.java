package net.wohlfart.gl.input;

/**
 * This package processes the user input
 *
 * InputSource:
 *   platform dependent low level input provider
 *
 * InputAdaptor:
 *   schema to turn low-level events into high-level events in the InputProcessor,
 *   part of the InputProcessor
 *
 * CommandEvent:
 *   contains the platform independent event hierarchy
 *
 * InputDispatcher:
 *   extends EventBus
 *   turns events from Input Source into high-level states and events
 *
 *   provides:
 *     - a event queue to register for high-level input events
 *     - a poll call to query the current input state
 *     - createEVents method to trigger event creation
 *
 *
 */
