//
//  BtmToolBar.m
//  HITRenIOSClient
//
//  Created by wubincen on 14-3-1.
//  Copyright (c) 2014年 wubincen. All rights reserved.
//

#import "BtmToolBar.h"

const int buttonCount = 5;

@implementation BtmToolBar

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
       
    }
    return self;
}

- (void)handleTouchesEvent:(UITouch*)touch {
    FUNC_START();
    CGPoint point = [touch locationInView:self];
    CGFloat blockwidth = self.frame.size.width / buttonCount;
    int index = 1 + (int)(point.x / blockwidth);
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:[NSString stringWithFormat:@"temp%d", index] ofType:@"png"]];
    self.image = image;
    [self setNeedsDisplay];
    FUNC_END();

}

- (void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event {
    FUNC_START();
    UITouch* touch = [touches anyObject];
    CGPoint point = [touch locationInView:self];
    LOG(@"%f %f", point.x, point.y);
    CGFloat blockwidth = self.frame.size.width / buttonCount;
    LOG(@"blockwidth %f", blockwidth);
    int index = 1 + (int)(point.x / blockwidth);
    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:[NSString stringWithFormat:@"p_btm%d", index] ofType:@"png"]];
    self.image = image;
    [self setNeedsDisplay];
    FUNC_END();
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
    FUNC_START();
//    UITouch* touch = [touches anyObject];
//    CGPoint point = [touch locationInView:self];
//    CGFloat blockwidth = self.frame.size.width / buttonCount;
//    int index = 1 + (int)(point.x / blockwidth);
//    UIImage* image = [UIImage imageWithContentsOfFile:[[NSBundle mainBundle] pathForResource:[NSString stringWithFormat:@"r_btm%d", index] ofType:@"png"]];
//    self.image = image;
//    [self setNeedsDisplay];
    FUNC_END();
}   

- (void)touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event {
    
}

- (void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event {
    
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
