class Target {
 int id;
 PVector top; //Centre coordinates of each side of the square
 PVector bottom;
 PVector left;
 PVector right;
 
 float size;
 
 float fittsID;
  
 Target(int id, PVector top, PVector bottom, PVector left, PVector right, float size, PVector virtual) {
  this.id = id;
  this.top = top;
  this.bottom = bottom;
  this.left = left;
  this.right = right;
  this.size = size;
  PVector centre = new PVector((top.x + bottom.x)/2.0, (top.y + bottom.y)/2.0);
   
  this.fittsID = log(dist(centre.x, centre.y, virtual.x, virtual.y)/this.size + 1) / log(2); //Base change formula
  
 }
 
 
}
