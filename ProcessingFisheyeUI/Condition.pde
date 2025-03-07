class Condition {
  //Class to hold condition data and results
  String name;
  int number_trials;
  int incomplete_trials = 0;
  float distortion_amount;
  
  float average_completion_time;
  float[] total_completion_times;
  float[] fitts_ids;
  int[] number_failures = new int[60];
  float[] total_mouse_movement = new float[60];
  
  Condition(String name, float amount) {
    this.name = name;
    this.distortion_amount = amount;

    this.number_trials = 60;
    
    total_completion_times = new float[60];
    fitts_ids = new float[60];
  }
  
  String yield_data(int i) {
    //Printing options of final data
    
    println(this.name + ", " + str(i-4) + ", "  + str(this.fitts_ids[i]) + ", " + str(this.total_completion_times[i]) + ", " + str(this.number_failures[i]) + ", " + str(this.distortion_amount) + ", " + str(this.total_mouse_movement[i]));
    return this.name + ", " + str(i-4) + ", "  + str(this.fitts_ids[i]) + ", " + str(this.total_completion_times[i]) + ", " + str(this.number_failures[i]) + ", " + str(this.distortion_amount) + ", " + str(this.total_mouse_movement[i]);


    
  }
}
