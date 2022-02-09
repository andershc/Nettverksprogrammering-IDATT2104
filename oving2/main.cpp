#include <iostream>
#include "Workers.h"

using namespace std;


int main() {
    Workers worker_threads(4);
    Workers event_loop(1);
    worker_threads.start(); // Create 4 internal threads
    event_loop.start(); // Create 1 internal thread
    worker_threads.post([] {
        // Task A
        cout << "Task A: parallel with B" << endl;
    });
    worker_threads.post_timeout([] {
        // Task B
        // Might run in parallel with task A
        cout << "Task B: parallel with A, I am slow" << endl;
    });
    event_loop.post([] {
        // Task C
        // Might run in parallel with task A and B
        cout << "Task C: Before D" << endl;
    });
    event_loop.post([] {
        // Task D
        // Will run after task C
        // Might run in parallel with task A and B
        cout << "Task D: waits for C" << endl;
    });

    worker_threads.stop();
    worker_threads.join(); // Calls join() on the worker threads
    event_loop.stop();
    event_loop.join(); // Calls join() on the event thread
}


