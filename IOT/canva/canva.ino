#include <TFT_eSPI_Widgets.h>
 
using namespace TFT_eSPI_Widgets;
 
// The TFT screen used for this demo
TFT_eSPI tft;
 
// The Canvas widget on which widgets are printed
Canvas canvas;
 
const char *simple_message = "Short message";
const char *long_message = "A long message that should be animated to be readable.";
const char *wrapped_message = "A long message that should fit on several lines without the need of some animation.";
const char *huge_message = "A so long message that even beeing wrapped, the entire screen is not enough to display it at once.\n\n"
  "This is why there is a vertical animation to read it until the end.";
 
unsigned long start_cycle;
 
// The arduino initialisation function
void setup() {
  Serial.begin(115200);
  while (!Serial) {
    delay(100);
  }
  Serial.println("Starting TFT_eSPI Widget library Message demo...");
  tft.init();
  tft.setRotation(1);
  canvas.init(tft,
              GraphicalProperties(TFT_DARKGREY /* background color */,
                                  TFT_RED /* border color */,
                                  TFT_WHITE /* font color */,
                                  2 /* border size */)
              );
 
  // Add a generic widget to the canvas such that this widget area
  // fits 90% of the full screen and is centered on it.
  Area area = canvas.getArea();
  area *= 0.9;
 
  // It is REQUIRED to create any child widget using the "new"
  // keyword.
  new GenericWidget(canvas, area);
  canvas.getChild().setPosition(50, 50);
 
  // Change the generic widget graphical properties.
  canvas.getChild().setDefaultGraphicalProperties(GraphicalProperties(TFT_WHITE /* background color */,
                                                                      TFT_BLUE /* border color */,
                                                                      TFT_BLACK /* font color */,
                                                                      4 /* border size */,
                                                                      2 /* font size */));
 
  // Add a message widget to the generic widget.
  //
  // It is REQUIRED to create any child widget using the "new"
  // keyword.
  new MessageWidget(canvas.getChild(), simple_message);
 
  // By default, the message graphical properties are inherited from
  // the generic widget properties.
 
  // A tip to add a margin is to define the border color of the
  // message widget being the same as the background color.
  GraphicalProperties p = canvas.getChild().getChild().getDefaultGraphicalProperties();
  p.setBorderColor(p.getBackgroundColor());
  canvas.getChild().getChild().setDefaultGraphicalProperties(p);
 
  // Here we play with the focus state by defining new colors when
  // message widget will have focus.
  p.setBackgroundColor(TFT_BLACK);
  p.setBorderColor(TFT_BLACK);
  p.setFontColor(TFT_WHITE);
  canvas.getChild().getChild().setFocusGraphicalProperties(p);
  canvas.getChild().getChild().setAcceptFocus(true);
 
  // Force redraw on next loop.
  canvas.touch();
 
  // Print widget tree on Serial.
  Serial.println("Widget tree:");
  canvas.print();
 
  Serial.println("[End of demo setup]");
  Serial.println("The first message will be prompted for only 5 seconds.");
  Serial.println("==========");
  start_cycle = millis();
 
}
 
// The arduino infinite loop function
void loop() {
 
  // Calling the loop() method will call the loop of any descendant
  // widget from the current canvas in the widget tree.
  canvas.loop();
  canvas.refresh();
 
  unsigned long now = millis() - start_cycle;
 
  // By default, getChild() will return a Widget (the base class of
  // any widget), but it is possible to specify the subclass of widget
  // in order to use specific methods (be aware that no control is
  // done, so any mistake on the subclass will be fatal).
  MessageWidget &w = canvas.getChild().getChild<MessageWidget>();
 
  // Since w is known to be a message widget, we can access to its
  // original message.
  String m = w.getOriginalMessage();
  if ((m == simple_message) and (now > 5000)) {
    // After 5 seconds, we replace the message of the message widget
    // by a new message.
    Serial.println("Replacement of the widget message:");
    Serial.print("old: ");
    Serial.println(m);
    // Here we change the message for a longer one.
    w.setMessage(long_message);
    Serial.print("new: ");
    Serial.println(w.getOriginalMessage());
    // The message widget must be redrawn on next loop.
    w.touch();
    Serial.println("This message will be prompted for 35 seconds.");
    Serial.println("==========");
  }
 
  // We follow the same mechanism as before to set a new message, but
  // this message will be wrapped (play attention to the inner second
  // comment)
  if ((m == long_message) and (now > 40000)) {
    // After 35 more seconds, we replace the message of the message
    // widget by a new message.
    Serial.println("Replacement of the widget message:");
    Serial.print("old: ");
    Serial.println(m);
    w.setMessage(wrapped_message);
    // Here we change the message widget behavior. Now, the too large
    // messages will be wrapped.
    w.setWrap(true);
    Serial.print("new: ");
    Serial.println(w.getOriginalMessage());
    w.touch();
    Serial.println("This message will be prompted for 5 seconds.");
    Serial.println("==========");
  }
 
  // Here we simply set a so huge text that we should observe vertical
  // scrolling
  if ((m == wrapped_message) and (now > 45000)) {
    // After 5 more seconds, we replace the message of the message
    // widget by a new message.
    Serial.println("Replacement of the widget message:");
    Serial.print("old: ");
    Serial.println(m);
    w.setMessage(huge_message);
    Serial.print("new: ");
    Serial.println(w.getOriginalMessage());
    w.touch();
    Serial.println("This message will be prompted for 35 seconds.");
    Serial.println("==========");
  }
 
  // We just restart from the simple message, but change the focus
  // state of the message widget.
  if (now > 80000) {
    // After about 80 seconds, we replace the message of the message
    // widget by the simple message.
    Serial.println("Replacement of the widget message:");
    Serial.print("old: ");
    Serial.println(m);
    w.setMessage(simple_message);
    // We modify the message widget to not wrap the text.
    w.setWrap(false);
    Serial.print("new: ");
    Serial.println(w.getOriginalMessage());
    // And just for fun, we change the focus
    if (w.hasFocus()) {
      Serial.println("=> Unfocus the message widget.");
      w.unfocus();
      Serial.println("=> fit message widget to parent widget");
      w.fit();
    } else {
      Serial.println("=> Set focus on the message widget.");
      w.focus();
      Serial.println("=> Shrink message widget and position it at the bottom left corner of its parent.");
      w.shrink(0 /* left horizontal align */, 100 /* bottom vertical align */);
    }
    w.touch();
    Serial.println("This message will be prompted for 5 seconds.");
    Serial.println("==========");
    start_cycle = millis();
  }
 
}