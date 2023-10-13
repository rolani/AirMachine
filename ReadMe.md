## Air Machines
Air Machines, a new computing model for a cloud-fog-device system. 
An AM can be defined as a group of machines that are executing a given program. 
The primary difference between AM and existing systems that group large number of 
machines together to form systems (e.g., Network of Workstations (NOW)) is the
division of machines into infrastructure and non-infrastructure nodes in AM. 
The cloud and fog nodes are natural candidates for the infrastructure side. 
The non infrastructure side is composed of nodes sourced from the devices such as mobiles,
vehicles and other client-controlled (even fixed) machines.


### Experiment Setup

The vehicle movements in the experiments are recreated using a real taxi trace data from Rome for
30 days beginning February 2014. Dashcam videos are used to model the video feeds from the vehicles. 

The taxi trace data set is preprocessed in the following way to determine the areas and time ranges that 
have sufficient amount of data. I split the area covered by the taxi trace into fixed size grids and the time range into 30 min blocks. 

There are 48 topologies in a day. I get all the topologies over the 30 days and calculate the mean and standard deviation of number of taxis within a given grid in each of the 48 topologies. Taxis represent devices and we place a fog in each grid and all fogs are connected to the
cloud.

I split the dashcam videos into 5s clips and generate a cumulative histogram to represent the distribution of the size of the clips. 
The video feeds from devices in the simulation are drawn from this distribution.


Devices capture videos as they move around based on the setup which could be AM or random. 

In the AM setup, the fog in a grid is responsible for coordinating the devices within the grid at any point in time. Coordination ranges from handling devices joining the grid to optimizing the video capture process. 
The fog coordinates the video capture by selecting a subset of devices. Whereas, in a random setup devices keep capturing videos and uploading them  to the fog which they are connected to with no coordination.

#### Parameters
The simulation parameters are as follow. 
(i) Time slot: amount of time elapsed before checking for a topology change. 

(ii) Failure rate: probability that a device will fail to upload captured data at a given time slot. 

(iii) Join time: expected duration for a device to join a fog and be ready to capture videos. 

(iv) Video feed size: the amount of video data generated and uploaded. 
