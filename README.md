# Infinite Recharge
The official repository of Team 6090's 2020 infinite recharge code, vision, and dashboard configurations.

To whom it may concern, a text file of all of our joystick button and axis mappings can be found in [`JoystickMap.txt`](JoystickMap.txt)

# Downloading
Download the source code with this command:

        $ git clone --recursive https://github.com/Team6090/InfiniteRecharge

The `--recursive` flag is important; it downloads all the submodule dependencies. If you do a regular git clone, then make sure you

        $ git submodule init
        $ git submodule update

to download the submodules. Every once and a while, you should run

        $ git submodule foreach git pull

To update the submodule code, ensuring you have the latest version.

## Building & Deploying
For those of you that are too lazy/impatient to read the rest of this document, just run this command in the root of the
repository to build the code:

        $ ./gradlew clean build -PuseSubmodules

If you want to deploy the code, run this command:

        $ ./gradlew clean build deploy -PuseSubmodules

Our workflow guide can be found [here](#contributing)

### Common Issues
- Gradle may fail to resolve some dependencies. If this happens, you'll need to run this command to manually download
them: 

        $ gradle downloadAll

- Gradle may not be able to find Java. Make sure you add `java` to the system path, and set the `JAVA_HOME` environment variable. You may have to restart your IDE after making these changes.

### Local Dependencies
The following dependencies have been set up to be built from source:

- [SwerveIO](https://github.com/Team6090/SwerveIO)
- [JLimelight](https://github.com/Team6090/JLimelight)
- [LibOI](https://github.com/Team6090/LibOI)

In the event you want to use a local copy of all of the above projects, specify the `useSubmodules` property to use the local source instead of the Maven dependencies.

For example:

        $ gradle clean build -PuseSubmodules

This command will compile and include a local copy of the projects so that you can make modifications and whatnot to the source code on your disk, then include the changes in your robot code. The projects are git submodules, so use git to keep them up to date.

## Competition Checklist
- [ ] Update Laptops
    - [ ] Windows Update
    - [ ] Code (always ` git pull`)
    - [ ] **See [versions table](https://github.com/Team6090/Software-Table/blob/master/README.md) for software updates**
- [ ] Ensure laptop functionality
    - [ ] Driver Station
    - [ ] Dashboard
    - [ ] Code can be modified and deployed
- [ ] Charge laptops

## Limelight Configurations
As well as code, this repository contains the LimeLight configurations used at competitions. These are found in the `limelight-conf/` directory. Each `.vpr` file is a pipeline, and can be uploaded to a Limelight for immediate use. As the Limelight configurations change, these pipeline files are updated.

## Shuffleboard
Shuffleboard is the experimental dashboard used this season. The exact layout used is in `shuffleboard.json`. As the Shuffleboard layout is changed, this file is updated.

## Contributing
Please view our [Code Of Conduct](CODE_OF_CONDUCT.md) and our [Contributing Guide](https://docs.google.com/document/d/1KQAkZUGQNtcGS0PK-z6KMCrwNERii5Keec-gO0QWUK0/edit?usp=sharing).

### Contributors
- Jordan Bancino
- Ethan Snyder
- Collin Heavner