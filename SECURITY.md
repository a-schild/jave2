# Security

## Reporting

Please report suspected vulnerabilities through
[a private security advisory](https://github.com/a-schild/jave2/security/advisories/new),
or by mail to andre@schild.ws. A reproducer is worth more than a description.

## What this library is

JAVE2 runs the `ffmpeg` executable and gives Java a typed way to build its command
line. It is a wrapper. Anything ffmpeg can do, an application that hands ffmpeg its
arguments can do, and that is the point of the library rather than a flaw in it.

That said, the boundary is worth stating exactly, because it decides what is the
library's problem and what is the calling application's.

## No shell is involved

Every process is started through `Runtime.exec(String[])`, the array form, which
passes arguments to the operating system as a list. **No command interpreter is
invoked at any point.** A file called `x; rm -rf /` is a file with an unusual name
and nothing else, on every platform.

There is no place where a command line is assembled by joining strings, and it
should stay that way. `ShellMetacharacterTest` in `jave-core-test` exists to keep
it honest: it reads a file named ``inj;&$(id)`id`&&whoami.avi``, encodes to a
target named ``out;&$(id)`id`.mp3``, and fails if anything is executed rather than
read or written.

## Paths are made absolute

Both the input and the output reach ffmpeg as absolute paths. This matters for a
second reason that has nothing to do with shells: ffmpeg parses its own command
line, and an argument starting with `-` is an option to it. A file called `-y`
would otherwise turn into one. Because the path is absolutised first it always
begins with a separator or a drive letter, and cannot be read as an option. That
is also covered by a test.

## What an application must not do

The library cannot protect against these, and no amount of filtering inside it
would, because they are all cases of the application choosing to give ffmpeg
instructions on a user's behalf.

- **Do not pass user input to `ProcessWrapper.addArgument`.** It adds an ffmpeg
  argument, faithfully, which is its job. An application that lets a user reach it
  has given the user control of ffmpeg.
- **Do not pass a user supplied URL to `MultimediaObject`** unless you intend the
  server to fetch it. ffmpeg speaks many protocols, so a URL is a request your
  server will make, and a crafted playlist can make it read files or reach hosts
  the user could not. Validate the scheme and the host first.
- **Do not let users choose the ffmpeg executable**, through `ProcessLocator` or by
  writing to the directory the bundled binary is extracted into. That is simply
  handing over the ability to run a program.
- **Treat a user supplied file name as data.** Store uploads under a name you
  generate and keep the original only as metadata.

## On CVE-2023-48909

This CVE, and the Snyk advisory SNYK-JAVA-WSSCHILD-6154599 derived from it,
describe the second category above rather than a defect in this code. The
[published proof of concept](https://github.com/Dollhouse-18/jave-core-Command-execution-vulnerability)
consists of an application passing attacker controlled input into ffmpeg arguments
and observing that ffmpeg then does what it was told.

The specific claim, that the library "does not filter the input commands and can
execute arbitrary commands", is answered by the two properties above: no shell is
ever spawned, and paths are absolutised so they cannot be read as options. Both are
covered by tests that fail if either stops being true.

None of this makes the underlying advice wrong. An application that exposes
ffmpeg's argument surface to its users has a real vulnerability, and it is that
application's to fix. The section above says how to avoid it.
