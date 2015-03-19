/*
 * Copyright 2015, Google Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the
 * distribution.
 *
 *    * Neither the name of Google Inc. nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.sjsu.cmpe273.lab2;

import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;

import java.util.logging.Logger;
import java.util.*;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class PollServer {
  private static final Logger logger = Logger.getLogger(PollServer.class.getName());

  /* The port on which the server should run */
  private int port = 50051;
  private ServerImpl server;

  private void start() throws Exception {
    server = NettyServerBuilder.forPort(port)
        .addService(PollServiceGrpc.bindService(new PollServiceImpl()))
        .build().start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        PollServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  private void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Main launches the server from the command line.
   */
  public static void main(String[] args) throws Exception {
    final PollServer server = new PollServer();
    server.start();
  }

  private class PollServiceImpl implements PollServiceGrpc.PollService {

    @Override
    public void createPoll(PollRequest req, StreamObserver<PollResponse> responseObserver) {
     
System.out.println(req.toString());
String Error="";
if("".equals(req.getModeratorId()))
{
	Error="Moderator ID Cannot be Empty";
}

if("".equals(req.getQuestion()))
{
	Error+="/n Question Cannot be Empty";
}

if("".equals(req.getStartedAt()))
{
	Error+="/n started_at cannot be empty";
}

if("".equals(req.getExpiredAt()))
{
	Error+="/n expired_at cannot be empty";
}
if(req.getChoiceCount()==0)
{
	Error+="/n Choices cannot be empty";
}
 	
 if(!"".equals(Error))
{

PollResponse id=PollResponse.newBuilder()
.setId(Error)
.build();

responseObserver.onValue(id);
}
else
{

Random rnd = new Random();
int count = 100000 + rnd.nextInt(90000);
String pollId="Poll ID : "+Integer.toString(count,36);

PollResponse id=PollResponse.newBuilder()
.setId(pollId)
.build();

responseObserver.onValue(id);

}
      
      responseObserver.onCompleted();
    }
  }



}