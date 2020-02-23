# Infinite Recharge
The official repository of Team 6090's 2020 infinite recharge code, vision, and dashboard configurations.

## Competition Checklist
- [ ] Update Laptops
    - [ ] Windows Update
    - [ ] Code (always ` git pull` and `git checkout` the current competition branch)
    - [ ] **See [versions table](https://github.com/Team6090/Software-Table/blob/master/README.md) for software updates**
- [ ] Ensure laptop functionality
    - [ ] Driver Station
    - [ ] Dashboard
    - [ ] Code can be modified and deployed
- [ ] Charge laptops

## Contributors
- Jordan Bancino
- Ethan Snyder
- Collin Heavner
- Jude Dobry
- Stephen Harnish

## Building & Deploying
### Common Issues
- Gradle may fail to resolve some dependencies. If this happens, you'll need to run this command to manually download
them: 

        $ gradle downloadAll

- Gradle may not be able to find Java. Make sure you add `java` to the system path, and set the `JAVA_HOME` environment variable. You may have to restart your IDE after making these changes.

### Local Dependencies
The following dependencies have been set up to allow the option of running locally:

- [SwerveIO](https://github.com/Team6090/SwerveIO)
- [JLimelight](https://github.com/Team6090/JLimelight)
- [LibOI](https://github.com/Team6090/LibOI)

In the even you want to use a local copy of any of the above projects, specify the `useLocalProject` property to clone it to the project directory.
For example:

        $ gradle clean build -PuseLocalSwerveIO

or:
        
        $ gradle clean build -PuseLocalJLimelight -PuseLocalLibOI

You can also use any combination of the above commands, or you can run this command to include all known dependencies:

        $ gradle clean build -PallLocal

These commands will compile and include a local copy of the project(s) so that you can make modifications and whatnot to the source code on your disk, then include the changes in your robot code. **On the first run, you may have to run the build twice
because the bootstrap routine may not successfully include the project**.

## Limelight Configurations
As well as code, this repository contains the LimeLight configurations used at competitions. These are found in the `limelight-conf/` directory. Each `.vpr` file is a pipeline, and can be uploaded to a Limelight for immediate use. As the Limelight configurations change, these pipeline files are updated.

## Shuffleboard
Shuffleboard is the experimental dashboard used this season. The exact layout used is in `shuffleboard.json`. As the Shuffleboard layout is changed, this file is updated.

## Tools

### `rio-network.sh`
Linux users do not need the Update Suite to deploy to a RoboRIO via ethernet or USB, but rather, can use this script to set up the DHCP server to properly connect. This script works over both USB and ethernet.

Usage:

    $ ./rio-network.sh eth0

Note that this needs to be run as `root`, and of course provide the interface that you're connected to, either USB or Ethernet. You should not need this tool for deploying wirelessly as the radio will automatically  take care of DHCP operations.

## Contributing
Please view our [Code Of Conduct](CODE_OF_CONDUCT.md). Eventually we will add a contributing guide. Eventually. Maybe. Possibly.
