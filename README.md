# StaffNecessities
> Various utilities for controlling chat. An essential plugin for any server! Fully configurable sounds, permissions and messages.

This plugin was a request by *Kelzark* for the ***Hydrus*** network.

## Installation
Just go and grab the latest JAR files off of the [releases page](https://github.com/Rayzr522/StaffNecessities/releases)!

## Configuring
All permissions, messages and command sounds are configurable. A list of valid sounds to be used is generated when the plugin is first run, due to the fact that the `Sound` enum values are different in 1.8 and below versus 1.9+.

## Commands

### `/staffnecessities`
Aliases: `/sn`, `/chatcontrol`, `/cc`  
Permission: `StaffNecessities.use`  
Description: The base command for **StaffNecessities**.

### `/staffnecessities staff`
Permission: `StaffNecessities.chat.staff` 
Description: Enables **staff-chat** mode. Only other users that have staff-chat mode enabled will see your messages.

### `/staffnecessities mute`
Permission: `StaffNecessities.chat.mute`  
Description: Mutes the chat globally. Completely disables all messages (except staff-chat messages, and of course any command-based message systems like private messages).

### `/staffnecessities slow`
Permission: `StaffNecessities.chat.slow`  
Description: Enables **rate-limiting** of chat. The minimum time between messages is configurable.

### `/staffnecessities clear`
Permission: `StaffNecessities.chat.clear`  
Description: Clears the chat.

### `/staffnecessities reload`
Permission: `StaffNecessities.admin.reload`  
Description: Reloads the config and message files.

## Join Me
[![Discord Badge](https://github.com/Rayzr522/ProjectResources/raw/master/RayzrDev/badge-small.png)](https://discord.io/rayzrdevofficial)
