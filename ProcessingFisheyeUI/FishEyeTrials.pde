/*
Tyler Boechler

When ran will do 60 target selections with normal fisheye, then will do 60 more with speed dependent distortion reduction fisheye
Calculates Fitts ID, other useful metrics for analysis

SDDR technique explained on line 68
*/

import java.util.LinkedList;
// Storage for running average
LinkedList<Float> speed_list;

//State
enum SystemState {BEFORE_CONDITION, TRIAL, DONE};

SystemState phase = SystemState.BEFORE_CONDITION;

//Trial global variables
Target[] targets;
int one_to_click;
float start_time;

Condition[] conditions;
int current_trial = 1;
int current_condition_index = 0;
int number_conditions = 2;
int num_errors = 0;
float distortion_amount = 0;
float mouse_movement_distortion_amplification = 1;
float movement_start_time = 0;
float last_distortion = 1;

float speed_size = 10.0; //SDDR variable
int speed_direction = -1;

PVector position, displacement, previous_position;


void setup() {
  println("Name, Test #, Fitts id, completion time, num failures, distortion, total_mouse_movement");
  fullScreen();
  //assert width >= 1080 && height >= 1080;
  speed_list = new LinkedList<Float>();
  frameRate(30); // Set to 30 FPS

  
  position = new PVector(mouseX, mouseY); // Store position (Every time i use position i reset it to newest mouseX, mouseY)
  displacement = new PVector(0, 0); // Mouse movement
  previous_position = new PVector(mouseX, mouseY); // Previous position stored for displacement tracking through mousemove event calls
  generateTargets();
  
  //Generate conditions
  conditions = new Condition[number_conditions];

  conditions[0] = new Condition("Normal", 2);
  conditions[0] = new Condition("SDDR", 2);

};

void mouseMoved() {
  if (phase == SystemState.DONE) {
    return;
  }
  
  position = new PVector(mouseX, mouseY);
  displacement.x = position.x - previous_position.x;
  displacement.y = position.y - previous_position.y;
  
  /* SDDR is calculated by competing functions. 
  A variable named speed size, with enforced bounds of [10.0, 100.0] is constantly decreased in the draw loop
  The mousemove calls here use a running average of mouse displacement over time, and will increase this variable if the mouse moves quicker
  The calls here can also decrease the speed size if moving slowly to help the effect snap back quicker (still smooth)
  Speed size is used as:
  SDDR_Distortion = sqrt(speed_size/10.0)
  Distortion_level /= SDDR_Distortion
  
  When speed_size = 10, there is no change
  */
  if (conditions[current_condition_index].name.equals("SDDR") && distortion_amount != 0) { //off when not in use for computation efficiency
    if (speed_list.size() > 8) { // Only calculate when we have enough data for stability
      float avg = 0;
      for(float s: speed_list) {
        avg += s;
      }
      avg /= speed_list.size();
      
      //println(avg);
      if (avg < 9) {  // Moving too slow, reduce distortion
        speed_direction = -1; // Draw loop should have heavy limiting
        speed_size -= 1.5;
      }
      if (avg < 12) { // Moving a little faster, start to increase distortion (still limited by draw loop when no SDDR)
        speed_size += 1.5;
      }
      else if (avg < 15) { // Moving quickly, change direction to turn down draw loop limiting 
        speed_direction = 1;
        speed_size += 2;
      }
      else { // Moving very fast
        speed_direction = 1;
        speed_size += 3;
      }
      speed_list.removeFirst(); // Remove first item
      
    }
    speed_list.add(displacement.mag());// Add new item to displacement list
  }
  if (current_trial > 5) {
    conditions[current_condition_index].total_mouse_movement[current_trial-1] += sqrt(pow(displacement.y, 2) + pow(displacement.x, 2)); // Record total mouse movement for trial
  }
  //Reset variables
  displacement.x = 0;
  displacement.y = 0;
  previous_position = position.copy();
  
  return;
};


Target within_any_targets() { 
  /*
   Checks if current position is within any target
   Returns the target the cursor is within, or else null
 */
  for (Target t: targets) {
    position = new PVector(mouseX, mouseY);
    if (position.x > t.left.x && position.x < t.right.x && position.y > t.top.y && position.y < t.bottom.y) { // Rectangle intersection
      return t;
    }
  }
  return null;
}

void recalculate_fitts_ids() {
  /**
  Upon new trial, recalculate the id values of every target to the current mouse position
  */
  position = new PVector(mouseX, mouseY);
  for (Target t: targets) {
    PVector centre = new PVector((t.top.x + t.bottom.x)/2.0, (t.top.y + t.bottom.y)/2.0);
    t.fittsID = log(dist(centre.x, centre.y, position.x, position.y)/t.size + 1) / log(2); //Base change formula
  }
}

void mousePressed() {
  switch (phase) {
    case BEFORE_CONDITION:
      //Start trial init variables
      distortion_amount = conditions[current_condition_index].distortion_amount;
      phase = SystemState.TRIAL;
      start_time = millis();
      mouse_movement_distortion_amplification = 1;
      break;
      
    case TRIAL:
     if (speed_size == 10.0 || distortion_amount == 0) { // Only allow clicks/errors when screen is not zooming (always true in non sddr)
     //Get target clicked
     Target selected = within_any_targets();
     if (selected != null) {
       if (selected.id == one_to_click) {
         if (current_trial > 5) {
           //Record data after first 5 training samples
           conditions[current_condition_index].total_completion_times[current_trial-1] = (millis() - start_time)/1000;
           conditions[current_condition_index].fitts_ids[current_trial-1] = selected.fittsID;
           conditions[current_condition_index].yield_data(current_trial-1);
           recalculate_fitts_ids();
       }
         start_time = millis(); //Restart timer
         current_trial += 1;
         one_to_click = round(random(0, 29));
       }
       else {
         conditions[current_condition_index].number_failures[current_trial-1] += 1; //Wrong target
       }
     }
     else {
       conditions[current_condition_index].number_failures[current_trial-1] += 1; //Missed all targets
     }
     }
      break;
     
    case DONE:
      cursor(); //Cursor returns
      break;
    
    default:
      println("Unknown state");
      break;
  }
};



boolean isValidTarget(Target t) {
  /*
  Helper function for generating targets,
  With input Target t, will return boolean if the provided input is valid on the canvas
  Checks for intersection with any pre-existing targets
  */
  Target comparison;
  for (int i = 0; i < t.id; i++) {
    comparison = targets[i];
    if (t.right.x >= comparison.left.x && t.left.x <= comparison.right.x && t.bottom.y >= comparison.top.y && t.top.y <= comparison.bottom.y) { // Check rectangle intersection with all other targets
      return false;
    }
    
  }
  return true;
}

Target generateRandomTarget(int id) {
  /*
  Creates a random target in the testing space with provided id as per specifications
  */
  float random_size = random(20, 100);
  float random_x = random((random_size+2)/2.0, width - (random_size+2)/2.0);
  float random_y = random((random_size+2)/2.0, height - (random_size+2)/2.0);
  //int id, PVector top, PVector bottom, PVector left, PVector right, float size, PVector true_position
  return new Target(id, new PVector(random_x, random_y - random_size/2.0), new PVector(random_x, random_y + random_size/2.0), new PVector(random_x - random_size/2.0, random_y), new PVector(random_x + random_size/2.0, random_y), random_size, position);
}

void generateTargets() {
  /*
  Generate a new set of 30 random targets
  */
  targets = new Target[30];
  boolean invalid_target;
  one_to_click = round(random(0, 29));
  for(int i = 0; i < 30; i++) {
    Target t = null;
    invalid_target = true;
    while (invalid_target) {
      //Keep generating targets until they fit
      t = generateRandomTarget(i);
      invalid_target = ! isValidTarget(t);
    }

    targets[i] = t;
  }
}

void drawTargets() {
  /*
  Draw all targets, show specific click target as green
  */
  for(Target t: targets) {
    color c;
    if (t.id == one_to_click) {
      if (speed_size == 10) {
        stroke(color(0, 0, 255));
        strokeWeight(4);
      }
      c = color(0, 255, 0);
    }
    else {
      c = color(255, 255, 0);
    }
    fill(c);
    PVector c_tl = calculateDistortion(new PVector(t.left.x, t.top.y)); // top left
    PVector c_tr = calculateDistortion(new PVector(t.right.x, t.top.y)); // top right
    PVector c_br = calculateDistortion(new PVector(t.right.x, t.bottom.y)); // bottom right
    PVector c_bl = calculateDistortion(new PVector(t.left.x, t.bottom.y)); // bottom left
    quad(c_tl.x, c_tl.y, c_tr.x, c_tr.y, c_br.x, c_br.y, c_bl.x, c_bl.y);
    stroke(0);
    strokeWeight(1);
  }
}


void drawGridLines() {
  /*
  Draw grid lines, spaced by 100 px
  */
  for (int x = 0; x < width + 100; x +=100) {
    for(int y = 0; y < height + 100; y += 100) {
      fill(color(150, 150, 150));
      PVector line1 = calculateDistortion(new PVector(0, y));
      PVector line2 = calculateDistortion(new PVector(x, y));
      PVector line3 = calculateDistortion(new PVector(x, 0));
      
      line(line1.x, line1.y, line2.x, line2.y);
      line(line3.x, line3.y, line2.x, line2.y);
    }
  }
}

PVector calculateDistortion(PVector point) {
  /*
  Given PVector point, return a PVector of the distorted position
  */
  position = new PVector(mouseX, mouseY);
  
  mouse_movement_distortion_amplification = sqrt(speed_size/10.0);
  float d = distortion_amount / mouse_movement_distortion_amplification;
  
  d = lerp(d, last_distortion, 0.2); //Smooth transitions
  last_distortion = d;
  
  PVector focus = position;
  float pfx = 0;
  float pfy = 0;
  float dx, dy;
  float gx, gy;
  
  float maxDiffx, maxDiffy;
  
  
  if (point.equals(focus) || d == 0) {
    return point;
  }
  
  //Calculate fisheye distortion
  if (point.x < focus.x) {
    maxDiffx = focus.x;
    dx = abs(point.x - focus.x);
    gx = (d + 1) / (d + maxDiffx / dx);
    pfx = focus.x - gx * maxDiffx;
  }
  else if (point.x > focus.x) {
    maxDiffx = width - focus.x;
    dx = abs(point.x - focus.x);
    gx = (d + 1) / (d + maxDiffx / dx);
    pfx = focus.x + gx * maxDiffx;
  }
  
  if (point.y < focus.y) {
    maxDiffy = focus.y;
    dy = abs(point.y - focus.y);
    gy = (d + 1) / (d + maxDiffy / dy);
    pfy = focus.y - gy * maxDiffy;
  }
  else if (point.y > focus.y) {
    maxDiffy = height - focus.y;
    dy = abs(point.y - focus.y);
    gy = (d + 1) / (d + maxDiffy / dy);
    pfy = focus.y + gy * maxDiffy;
  }
  
  return new PVector(pfx, pfy);
}

void draw() {
  background(200); // Clear the canvas
  fill(255);
  stroke(1);
  position = new PVector(mouseX, mouseY);
  if (current_condition_index >= number_conditions) {
    phase = SystemState.DONE;  
    return;
  }
  //println(speed_size);
  //println("-");
  if (conditions[current_condition_index].name.equals("SDDR") && distortion_amount != 0) {
    if (speed_direction == -1) { 
      speed_size -= 3; // Keep no SDDR strict
    }
    else {
      speed_size -= 1; // Allow SDDR easier
    }
    //Enforce boundaries
    if (speed_size < 10) {
      speed_size = 10;
    }
    else if (speed_size > 100) {
      speed_size = 100;
    }
    //ellipse(position.x, position.y, speed_size, speed_size); //Debugging visualization
  }
  
  
  switch (phase) {
    case BEFORE_CONDITION:
      stroke(5);
      fill(0);
      text("Click to start trial: " + conditions[current_condition_index].name + " "  +str(conditions[current_condition_index].distortion_amount), width/3, height/2);
      stroke(1);
      break;
      
    case TRIAL:
      stroke(5);
      text(str(current_trial), 20, 20); //Little counter for trials
      stroke(1);
      if (current_trial > 60) {
        //Update condition
        current_condition_index += 1;
        current_trial = 1;
        if (current_condition_index >= number_conditions) {
          //Done
          phase = SystemState.DONE;
          return;
        }
        //Generate new set of targets for next condition
        generateTargets();
        phase = SystemState.BEFORE_CONDITION;
      }
      
      drawGridLines();
      drawTargets();
      
      break;
    case DONE:
      break;
    default:
      println("Unknown state");
      break;
  }
  fill(color(255, 255, 0));
};
